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

    private val redPaint = Paint().apply {
        color = Color.RED
    }

    private val ampList = mutableListOf<Float>()
    private val rectList = mutableListOf<RectF>()

    /**
     * 그림 그림
     * (0, 0) -> 왼쪽 상단
     */
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        for(recF in rectList) {
            canvas.drawRect(recF, redPaint)
        }
    }

    /**
     * 위를 기준으로 아래로 늘어나고 줄어들음
     */
    fun addAmplitude(maxAmplitude: Float) {

        rectList.clear() // 사각형 리스트는 계속 쌓지 않고 최신 데이터만 저장하게 갱신함
        ampList.add(maxAmplitude) // 진폭 값 누적해서 저장

        val rectWidth = 10f // 한개의 사각형 너비 todo UI에 따라 값 바꾸기
        val maxRect = (this.width/rectWidth).toInt() // this.width = 전체 뷰, 최대 몇개의 사각형이 들어갈 수 있는가?

        val amps = ampList.takeLast(maxRect)  // 시간이 갈수록 계속 추가되는 리스트, 뷰에 보이는 것만 가져옴 (가장 최근 데이터)

        for((i, amp) in amps.withIndex()) {
            val rectF = RectF()
            rectF.top = 0f
            rectF.bottom = amp
            rectF.left = i * rectWidth // 이전 사각형의 right
            rectF.right = rectF.left + rectWidth
            rectList.add(rectF)
        }

        invalidate() // UI 초기화 → onDraw 함수 호출
    }
}