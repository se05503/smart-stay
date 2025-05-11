package com.example.smartstay

import android.app.Application
import com.kakao.sdk.common.KakaoSdk

class SmartStayApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        KakaoSdk.init(this, "fe73280b622e53de8942ca28b79b4781")
    }
}