package com.example.smartstay

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import com.navercorp.nid.NaverIdLoginSDK

class SmartStayApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        KakaoSdk.init(this, getString(R.string.kakao_login_key))
        NaverIdLoginSDK.initialize(
            context = applicationContext,
            clientId = getString(R.string.naver_login_client_id),
            clientSecret = getString(R.string.naver_login_client_secret),
            clientName = getString(R.string.naver_login_client_name)
        )
    }
}