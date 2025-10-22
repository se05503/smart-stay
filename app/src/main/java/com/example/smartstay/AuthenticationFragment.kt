package com.example.smartstay

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.smartstay.databinding.FragmentAuthenticationBinding
import com.example.smartstay.model.SocialLoginRequest
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import androidx.fragment.app.activityViewModels
import com.example.smartstay.model.UserModel
import com.example.smartstay.network.RetrofitInstance
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.oauth.OAuthLoginCallback
import com.navercorp.nid.profile.NidProfileCallback
import com.navercorp.nid.profile.data.NidProfileResponse

class AuthenticationFragment : Fragment(R.layout.fragment_authentication) {

    private lateinit var binding: FragmentAuthenticationBinding
    private val userViewModel: UserViewModel by activityViewModels()
    private val loginViewModel: LoginViewModel by activityViewModels {
        LoginViewModelFactory(RetrofitInstance.networkService)
    }
    private lateinit var googleLoginResult: ActivityResultLauncher<Intent>
    private var naverRefreshToken: String? = null
    private var naverAccessToken: String? = null

    /**
     * 뷰와 관련없는 초기화 작업을 하기에 적합한 생명주기
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /**
         * 카카오/네이버는 앱 클라이언트 내에서 refreshToken, accessToken 을 클라이언트에서 받을 수 있다
         * 구글은 serverAuthCode 를 클라이언트가 서버에 넘겨줘서 서버가 refreshToken, accessToken 을 발급해야한다.
         * 구글은 클라이언트에서 idToken 만 발급받을 수 있다.
         */
        googleLoginResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

            val resultCode: Int = result.resultCode
            val intent: Intent? = result.data

            val task = GoogleSignIn.getSignedInAccountFromIntent(intent)
            val account = task.getResult(ApiException::class.java)

            Log.e(
                GOOGLE_LOGIN,
                "accountId: ${account.id}, idToken: ${account.idToken}, serverAuthCode: ${account.serverAuthCode}, email: ${account.email}, photoUrl: ${account.photoUrl}, displayName: ${account.displayName}"
            )

            loginViewModel.postSocialLogin(
                socialLoginRequest = SocialLoginRequest(
                    provider = "google",
                    user_id = account.id ?: "",
                    email = account.email ?: "",
                    nickname = account.displayName ?: "",
                    refreshToken = "${account.idToken}",
                    accessToken = account.serverAuthCode ?: ""
                )
            )

            userViewModel.userInfo = UserModel(
                id = account.id ?: "",
                nickname = account.displayName ?: "",
                imageUrl = account.photoUrl.toString()
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAuthenticationBinding.bind(view)
        initListeners()
        initObservers()
    }

    private fun initObservers() {
        loginViewModel.socialLoginInfo.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                Toast.makeText(context, "소셜 로그인이 성공했습니다.", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_navigation_authentication_to_navigation_initial_setting_start)
            }.onFailure { error ->
                Toast.makeText(context, error.message ?: "소셜 로그인에 실패했습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun initListeners() = with(binding) {
        ivLoginKakao.setOnClickListener {
            val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
                if (error != null) {
                    Toast.makeText(context, "카카오계정으로 로그인 실패", Toast.LENGTH_SHORT).show()
                    Log.e(KAKAO_LOGIN, error.message ?: "no message")
                } else if (token != null) {
                    Toast.makeText(context, "카카오계정으로 로그인 성공", Toast.LENGTH_SHORT).show()
                    Log.e(KAKAO_LOGIN, token.accessToken)
                }
            }
            if (UserApiClient.instance.isKakaoTalkLoginAvailable(requireContext())) {
                UserApiClient.instance.loginWithKakaoTalk(requireContext()) { token, error ->
                    if (error != null) {
                        Toast.makeText(context, "카카오톡으로 로그인 실패", Toast.LENGTH_SHORT).show()
                        Log.e(KAKAO_LOGIN, error.message ?: "no message")
                        if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                            return@loginWithKakaoTalk
                        }
                        UserApiClient.instance.loginWithKakaoAccount(
                            requireContext(),
                            callback = callback
                        )
                    } else if (token != null) {
                        Log.e(KAKAO_LOGIN, "카카오톡으로 로그인 성공 ${token.accessToken}")
                        UserApiClient.instance.me { user, meError ->
                            if (meError != null) {
                                Log.e(KAKAO_LOGIN, "사용자 정보 요청 실패", meError)
                            } else if (user != null) {
                                loginViewModel.postSocialLogin(
                                    socialLoginRequest = SocialLoginRequest(
                                        provider = "kakao",
                                        user_id = user.id.toString(),
                                        email = user.kakaoAccount?.email ?: "",
                                        nickname = user.kakaoAccount?.profile?.nickname ?: "",
                                        refreshToken = token.refreshToken,
                                        accessToken = token.accessToken
                                    )
                                )
                                userViewModel.userInfo = UserModel(
                                    id = user.id.toString(),
                                    nickname = user.kakaoAccount?.profile?.nickname ?: "",
                                    imageUrl = user.kakaoAccount?.profile?.profileImageUrl ?: ""
                                )
                            }
                        }
                    }
                }
            } else {
                UserApiClient.instance.loginWithKakaoAccount(requireContext(), callback = callback)
            }
        }
        ivLoginNaver.setOnClickListener {
            val profileCallback = object : NidProfileCallback<NidProfileResponse> {
                override fun onSuccess(response: NidProfileResponse) {

                    val userId = response.profile?.id ?: ""
                    val nickname = response.profile?.nickname ?: ""
                    val email = response.profile?.email ?: ""
                    val profileImage = response.profile?.profileImage ?: ""
                    val gender = response.profile?.gender
                    val name = response.profile?.name
                    val age = response.profile?.age
                    val birthday = response.profile?.birthday
                    val birthYear = response.profile?.birthYear

                    Log.e(
                        NAVER_LOGIN,
                        "id: $userId, nickname: $nickname, email: $email, profileImage: $profileImage, gender: $gender, name: $name, age: $age, birthday: $birthday, birthYear: $birthYear"
                    )

                    loginViewModel.postSocialLogin(
                        socialLoginRequest = SocialLoginRequest(
                            provider = "naver",
                            user_id = userId,
                            email = email,
                            nickname = nickname,
                            refreshToken = naverRefreshToken ?: "",
                            accessToken = naverAccessToken ?: ""
                        )
                    )

                    userViewModel.userInfo = UserModel(
                        id = userId,
                        nickname = nickname,
                        imageUrl = profileImage
                    )
                }

                override fun onError(errorCode: Int, message: String) {
                    Log.e("naver profile", "errorCode: $errorCode, message: $message")
                }

                override fun onFailure(httpStatus: Int, message: String) {
                    Log.e("naver profile", "httpStatus: $httpStatus, message: $message")
                }
            }

            val oauthLoginCallback = object : OAuthLoginCallback {
                override fun onSuccess() {
                    naverAccessToken = NaverIdLoginSDK.getAccessToken()
                    naverRefreshToken = NaverIdLoginSDK.getRefreshToken()
                    Log.e(
                        NAVER_LOGIN,
                        "accessToken: $naverAccessToken, refreshToken: $naverRefreshToken"
                    )
                    NidOAuthLogin().callProfileApi(profileCallback)
                }

                override fun onError(errorCode: Int, message: String) {
                    Log.e(NAVER_LOGIN, "errorCode: $errorCode, message: $message")
                }

                override fun onFailure(httpStatus: Int, message: String) {
                    Log.e(
                        NAVER_LOGIN,
                        "httpStatus: ${NaverIdLoginSDK.getLastErrorCode()}, message: ${NaverIdLoginSDK.getLastErrorDescription()}"
                    )
                }
            }

            NaverIdLoginSDK.authenticate(requireContext(), oauthLoginCallback)
        }
        ivLoginGoogle.setOnClickListener {
            val googleSignInOption =
                GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.google_server_client_id))
                    .requestServerAuthCode(getString(R.string.google_server_client_id))
                    .requestEmail()
                    .requestId()
                    .requestProfile()
                    .build()
            val googleSignInClient = GoogleSignIn.getClient(requireContext(), googleSignInOption)
            googleLoginResult.launch(googleSignInClient.signInIntent)
        }
    }

    companion object {
        const val NAVER_LOGIN = "NAVER"
        const val KAKAO_LOGIN = "KAKAO"
        const val GOOGLE_LOGIN = "GOOGLE"
    }

}