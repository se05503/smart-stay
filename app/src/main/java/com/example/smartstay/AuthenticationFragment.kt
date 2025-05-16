package com.example.smartstay

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.smartstay.databinding.FragmentAuthenticationBinding
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient

class AuthenticationFragment: Fragment(R.layout.fragment_authentication) {

    private lateinit var binding: FragmentAuthenticationBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAuthenticationBinding.bind(view)
        binding.ivLoginKakao.setOnClickListener {
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
                        Toast.makeText(requireContext(), "${token.accessToken}", Toast.LENGTH_SHORT).show()
                        Toast.makeText(requireContext(), "로그인에 성공했습니다!", Toast.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.action_navigation_authentication_to_navigation_initial_setting_start)
                    }
                }
            } else {
                UserApiClient.instance.loginWithKakaoAccount(requireContext(), callback = callback)
            }
        }
    }
}