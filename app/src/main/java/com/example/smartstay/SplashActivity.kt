package com.example.smartstay

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.airbnb.lottie.LottieAnimationView

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val lottieView = findViewById<LottieAnimationView>(R.id.lottie_splash)
        lottieView.speed = 2.5f

        lottieView.addAnimatorListener(object: AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                val intent = Intent(this@SplashActivity, EntryActivity::class.java)
                startActivity(intent)
                finish() // 이전 키를 눌렀을 때 스플래시 화면으로 이동하는 것 방지
            }
        })
    }
}