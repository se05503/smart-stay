package com.example.smartstay.presentation.chat

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View

/**
 * @JvmOverloads: 여러개의 생성자가 자바 코드에서 보일 수 있게 설정
 *
 */
class VoiceWaveformView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defaultStyleAttribute: Int = 0
): View(context, attributeSet, defaultStyleAttribute) {

    // 절대 좌표 (20, 30), (50, 30), (20, 90), (50,90)
    // 사각형 width: 30, height: 60
    val rectF = RectF(20f, 30f, 20f + 30f, 30f + 60f) // 초기값
    val redPaint = Paint().apply {
        color = Color.RED
    }

    /**
     * 그림 그림
     * (0, 0) -> 왼쪽 상단
     */
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawRect(rectF, redPaint)
    }

    /**
     * 위를 기준으로 아래로 늘어나고 줄어들음
     */
    fun addAmplitude(maxAmplitude: Float) {
        rectF.top = 0f // 제일 위에 붙이기
        rectF.bottom = maxAmplitude
        rectF.left = 0f
        rectF.right = rectF.left + 20f // width: 20f
        invalidate() // UI 초기화 → onDraw 함수 호출
    }
}