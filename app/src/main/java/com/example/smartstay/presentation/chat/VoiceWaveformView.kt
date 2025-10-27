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
    private val maxRect by lazy { (this.width/rectWidth).toInt() } // this.width = 전체 뷰, 최대 몇개의 사각형이 들어갈 수 있는가?
    private var tick = 0

    /**
     * 실제 뷰에 그림을 그림
     */
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        for(recF in rectList) {
            canvas.drawRect(recF, redPaint)
        }
    }

    /**
     * (1) maxAmplitude / Short.MAX_VALUE = 정규화 과정 (0 < x < 1)
     * maxAmplitude는 자료형이 Int형(32비트)이지만, 실제 오디오 녹음 진폭은 16비트로 샘플링하고, 이는 Short형의 크기와 같다.
     * 따라서, maxAmplitude 가 원래 Int형이든, Float로 형변환했든 상관없이 Short의 최대값으로 나눠야 올바르게 0과 1사이로 정규화된다.
     * 결론적으로 알아야 할 점: MediaRecorder의 maxAmplitude의 값 범위는 Short형(16비트)이다.
     * 정규화 → 스케일링 → 조정 계수 곱하기
     * (2) y축은 아래(↓)로 갈수록 값이 커지고(+), 위(↑)로 갈수록 값이 작아진다(-)
     * (3) (this.height) / 2 + amp/2 와 동일함
     * (4) invalidate(기존 뷰를 무효화하다) = 뷰의 내용이 바뀌어서 화면을 다시 그려야 할 때 호출함 → onDraw 메소드 호출
     * (5) ampList.size <= 5 인 경우 rectList를 clear할 필요가 없다. 하지만 ampList.size > 5 인 경우, 새로운 값이 표시되어야 하고 이전 값은 표시가 되면 안되는 상황이 된다.
     * 만약 이때, clear 를 하지 않을 경우 기존 값을 빼줘야 한다.
     * removeFirst 메소드를 사용할 수는 있지만, 요구 API가 35로 비교적 높다. removeAt(0)을 사용할 수도 있지만, size가 5보다 작은지 큰지 분기를 쳐줘야 하는 번거로움이 있다.
     * 분기를 치는 것(if(rectList.size <= 5 ..)보다는 초기화(clear)하는 좀 더 편한 방식을 선택했다.
     * 참고로, 만약 이전 레코드 기록 UI도 스크롤로 보여줘야하는 상황이면 clear를 하면 안되고 누적해야한다.
     */
    fun addAmplitude(maxAmplitude: Float) {

        val scaledAmplitude = (maxAmplitude / Short.MAX_VALUE) * (this.height) * 0.8f // (1)

        rectList.clear() // (5)
        rectList.removeAt(0)
        ampList.add(scaledAmplitude)

        val visualAmpList = ampList.takeLast(maxRect)

        for((i, visualAmp) in visualAmpList.withIndex()) {
            val rectF = RectF()
            rectF.top = (this.height / 2) - visualAmp/2 // (2)
            rectF.bottom = rectF.top + visualAmp // (3)
            rectF.left = i * rectWidth
            rectF.right = rectF.left + rectWidth - 5f
            rectList.add(rectF)
        }

        invalidate() // (4)
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