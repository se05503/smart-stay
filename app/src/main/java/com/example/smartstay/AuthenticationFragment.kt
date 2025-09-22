package com.example.smartstay

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.smartstay.databinding.FragmentAuthenticationBinding
import com.example.smartstay.model.SocialLoginRequest
import com.example.smartstay.model.SocialLoginResponse
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import androidx.fragment.app.activityViewModels
import com.example.smartstay.model.UserInfo
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.oauth.OAuthLoginCallback
import com.navercorp.nid.profile.NidProfileCallback
import com.navercorp.nid.profile.data.NidProfileResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthenticationFragment: Fragment(R.layout.fragment_authentication) {

    private lateinit var binding: FragmentAuthenticationBinding
    private val viewModel: InitialSettingViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAuthenticationBinding.bind(view)

        // initialize
        binding.ivLoginNaver.setOnClickListener {
            val profileCallback = object: NidProfileCallback<NidProfileResponse> {

                override fun onSuccess(response: NidProfileResponse) {
                    val id = response.profile?.id
                    val nickname = response.profile?.nickname
                    val email = response.profile?.email
                    val profileImage = response.profile?.profileImage
                    val gender = response.profile?.gender
                    val name = response.profile?.name
                    val age = response.profile?.age
                    val birthday = response.profile?.birthday
                    val birthYear = response.profile?.birthYear
                    Log.e("naver profile", "id: $id, nickname: $nickname, email: $email, profileImage: $profileImage, gender: $gender, name: $name, age: $age, birthday: $birthday, birthYear: $birthYear")
                }

                override fun onError(errorCode: Int, message: String) {
                    Log.e("naver profile", "errorCode: $errorCode, message: $message")
                }

                override fun onFailure(httpStatus: Int, message: String) {
                    Log.e("naver profile", "httpStatus: $httpStatus, message: $message")
                }
            }

            val oauthLoginCallback = object: OAuthLoginCallback {

                override fun onSuccess() {
                    Toast.makeText(context, "네이버 로그인에 성공했습니다", Toast.LENGTH_SHORT).show()
                    val accessToken = NaverIdLoginSDK.getAccessToken()
                    val refreshToken = NaverIdLoginSDK.getRefreshToken()
                    Log.e("naver login", "accessToken: $accessToken, refreshToken: $refreshToken")
                    NidOAuthLogin().callProfileApi(profileCallback)
                }

                override fun onError(errorCode: Int, message: String) {
                    Log.e("naver login", "errorCode: $errorCode, message: $message")
                }

                override fun onFailure(httpStatus: Int, message: String) {
                    Log.e("naver login", "httpStatus: ${NaverIdLoginSDK.getLastErrorCode()}, message: ${NaverIdLoginSDK.getLastErrorDescription()}")
                }
            }

            NaverIdLoginSDK.authenticate(requireContext(), oauthLoginCallback)
        }

        initListeners()
    }

    private fun initListeners() = with(binding) {
        ivLoginKakao.setOnClickListener {
            val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
                if (error != null) {
                    Log.e("error", "카카오계정으로 로그인 실패", error)
                } else if (token != null) {
                    Log.i("error", "카카오계정으로 로그인 성공 ${token.accessToken}")
                    Toast.makeText(requireContext(), "${token.accessToken}", Toast.LENGTH_SHORT).show()
                }
            }

            if (UserApiClient.instance.isKakaoTalkLoginAvailable(requireContext())) {
                UserApiClient.instance.loginWithKakaoTalk(requireContext()) { token, error ->
                    if (error != null) {
                        Log.e("error", "카카오톡으로 로그인 실패", error)
                        if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                            return@loginWithKakaoTalk
                        }
                        UserApiClient.instance.loginWithKakaoAccount(requireContext(), callback = callback)
                    } else if (token != null) {
                        Log.i("error", "카카오톡으로 로그인 성공 ${token.accessToken}")
                        // 카카오계정 정보 가져오기
                        UserApiClient.instance.me { user, meError ->
                            if(meError != null) {
                                Log.e("kakao", "사용자 정보 요청 실패", meError)
                            } else if (user != null) {
                                val userInfo = UserInfo(
                                    provider = "kakao",
                                    user_id = user.id.toString(),
                                    email = user.kakaoAccount?.email,
                                    nickname = user.kakaoAccount?.profile?.nickname,
                                    imageUrl = user.kakaoAccount?.profile?.profileImageUrl
                                )
                                viewModel.userInfo = userInfo
                            }
                        }

                        // 서버 통신 되면 지우기(두줄 다)
//                        Toast.makeText(requireContext(), "로그인에 성공했습니다!", Toast.LENGTH_SHORT).show()
//                        findNavController().navigate(R.id.action_navigation_authentication_to_navigation_initial_setting_start)

                        val request = SocialLoginRequest(
                            provider = "kakao",
                            user_id = viewModel.userInfo.user_id,
                            email = viewModel.userInfo.email ?: "",
                            nickname = viewModel.userInfo.nickname ?: "",
                            refreshToken = token.refreshToken,
                            accessToken = token.accessToken
                        )

                        RetrofitInstance.networkService.postSocialLogin(request).enqueue(object: Callback<SocialLoginResponse> {
                            override fun onResponse(
                                call: Call<SocialLoginResponse?>,
                                response: Response<SocialLoginResponse?>
                            ) {
                                if(response.isSuccessful) {
                                    Toast.makeText(requireContext(), "카카오 로그인이 되었습니다!", Toast.LENGTH_SHORT).show()
                                    findNavController().navigate(R.id.action_navigation_authentication_to_navigation_initial_setting_start)
                                    Log.d("ttest(login)", response.body()?.message ?: "메세지 없음")
                                } else {
                                    Log.d("ttest(login)",""+response.errorBody()?.string())
                                }
                            }

                            override fun onFailure(
                                call: Call<SocialLoginResponse?>,
                                t: Throwable
                            ) {
                                Log.d("ttest(login)", ""+t.message)
                            }

                        })
                    }
                }
            } else {
                UserApiClient.instance.loginWithKakaoAccount(requireContext(), callback = callback)
            }
        }
    }

}