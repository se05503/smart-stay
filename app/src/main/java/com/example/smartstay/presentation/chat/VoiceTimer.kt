package com.example.smartstay.presentation.chat

import android.os.Handler
import android.os.Looper

/**
 * 사용자 목소리의 진폭을 주기적으로 받아오기 위해 필요함
 * handler 에 작업을 실행시켜 100ms 마다 runnable이 실행됨
 * 비유) handler: 관리자, runnable: 노동자(로봇)
 */
class VoiceTimer(listener: OnTimerTickListener) {
    private var duration: Long = 0L
    private val handler = Handler(Looper.getMainLooper())
    private val runnable: Runnable = object : Runnable { // 인터페이스(설계도) → object를 통해 구현해야함
        override fun run() {
            duration += 40L
            handler.postDelayed(this, 40L) // 100ms 단위로 무한루프 발생
            listener.onTick(duration)
        }
    }

    fun start() {
        handler.postDelayed(runnable, 40L)
    }

    fun stop() {
        handler.removeCallbacks(runnable)
    }
}

interface OnTimerTickListener {
    fun onTick(duration: Long)
}