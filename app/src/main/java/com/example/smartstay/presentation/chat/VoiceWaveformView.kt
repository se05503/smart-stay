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

    private val rectWidth = 15f
    private var tick = 0

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

        val amplitude = (maxAmplitude / Short.MAX_VALUE) * (this.height) * 0.8f // 값 보정. maxAmplitude / Short.MAX_VALUE: 0 ~ 1

        rectList.clear() // 사각형 리스트는 계속 쌓지 않고 최신 데이터만 저장하게 갱신함
        ampList.add(amplitude) // 진폭 값 누적해서 저장

        val maxRect = (this.width/rectWidth).toInt() // this.width = 전체 뷰, 최대 몇개의 사각형이 들어갈 수 있는가?

        val amps = ampList.takeLast(maxRect)  // 시간이 갈수록 계속 추가되는 리스트, 뷰에 보이는 것만 가져옴 (가장 최근 데이터)

        for((i, amp) in amps.withIndex()) {
            val rectF = RectF()
            rectF.top = (this.height / 2) - amp/2 // + ↓ / - ↑
            rectF.bottom = rectF.top + amp
            rectF.left = i * rectWidth
            rectF.right = rectF.left + rectWidth - 5f
            rectList.add(rectF)
        }

        invalidate() // UI 초기화 → onDraw 함수 호출
    }

    // 어느 시점을 재생하는지 알아야함
    // ampList: 0부터 재생이 멈춘 데까지 표현
    fun replayAmplitude() {
        rectList.clear()

        val maxRect = (this.width / rectWidth).toInt()
        val amps = ampList.take(tick).takeLast(maxRect)

        for((i, amp) in amps.withIndex()) {
            val recF = RectF()
            recF.top = (this.height)/2 - amp/2
            recF.bottom = recF.top + amp
            recF.left = i * rectWidth
            recF.right = recF.left + rectWidth - 5f // 5f: 여백을 위함
            rectList.add(recF)
        }

        tick++

        invalidate()
    }

    fun clearData() {
        ampList.clear()
    }

    fun clearWaveform() {
        rectList.clear()
        tick = 0
        invalidate()
    }
}