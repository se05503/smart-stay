package com.example.smartstay.presentation.chat

import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.example.smartstay.R
import com.example.smartstay.databinding.BottomSheetRecordBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import okio.IOException

class RecordBottomSheetFragment: BottomSheetDialogFragment(R.layout.bottom_sheet_record), OnTimerTickListener {

    private lateinit var binding: BottomSheetRecordBinding

    private var recorder: MediaRecorder? = null
    private var recordFileName: String = ""
    private var recordState: RecordState = RecordState.RELEASE

    private var mediaPlayer: MediaPlayer? = null
    private var playState: PlayState = PlayState.RELEASE

    private lateinit var timer: VoiceTimer

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = BottomSheetRecordBinding.bind(view)
        initViews()
        initListeners()
    }

    private fun initViews() = with(binding) {
        recordFileName = "${context?.externalCacheDir?.absolutePath}/smartstay.m4a" // /storage/emulated/0/Android/data/com.example.smartstay/cache/smartstay.mp3
        timer = VoiceTimer(this@RecordBottomSheetFragment)
        sivSendVoiceMessage.isEnabled = false
        sivSendVoiceMessage.alpha = 0.3f
    }

    private fun initListeners() = with(binding) {
        sivRecordVoiceState.setOnClickListener {
            when(recordState) {
                RecordState.RELEASE -> {
                    startRecording()
                }
                RecordState.RECORDING -> {
                    stopRecording()
                }
            }
        }
        ivPlayState.setOnClickListener {
            when(playState) {
                PlayState.RELEASE -> {
                    startPlaying()
                }
                PlayState.PLAYING -> {
                    pausePlaying()
                }
                PlayState.PAUSED -> {
                    resumePlaying()
                }
            }
        }
        tvBackToChat.setOnClickListener {
            dismiss()
        }
    }

    // 녹음을 시작하는 메소드
    private fun startRecording() = with(binding) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            recorder = MediaRecorder(requireContext()).apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                setOutputFile(recordFileName)
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            }
            try {
                recorder?.prepare()
                recorder?.start()
                timer.start()
                recordState = RecordState.RECORDING
                sivRecordVoiceState.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_stop_record))
                sivSendVoiceMessage.isEnabled = false
                sivSendVoiceMessage.alpha = 0.3f
                ivPlayState.isVisible = false
            } catch (e: IOException) {
                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show() // 파일 입출력 오류 (용량 부족, 파일 시스템 권한 X 등등..)
            }
        }
    }

    // 녹음을 중지하고 저장하는 메소드
    private fun stopRecording() = with(binding) {
        recorder?.stop()
        recorder?.release()
        recorder = null
        timer.stop()
        recordState = RecordState.RELEASE
        sivRecordVoiceState.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_record))
        sivSendVoiceMessage.isEnabled = true
        sivSendVoiceMessage.alpha = 1f
        ivPlayState.isVisible = true
    }

    private fun startPlaying() = with(binding) {
        mediaPlayer = MediaPlayer().apply {
            setDataSource(recordFileName)
            setOnCompletionListener {
                mediaPlayer?.release()
                mediaPlayer = null
                timer.stop()
                playState = PlayState.RELEASE
                ivPlayState.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_play))
            }
        }
        try {
            mediaPlayer?.prepare()
            mediaPlayer?.start()
            timer.start()
            playState = PlayState.PLAYING
            ivPlayState.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_pause))
        } catch (e: IOException) {
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun pausePlaying() = with(binding) {
        mediaPlayer?.pause()
        timer.stop()
        playState = PlayState.PAUSED
        ivPlayState.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_play))
    }

    private fun resumePlaying() = with(binding) {
        mediaPlayer?.start()
        timer.start()
        playState = PlayState.PLAYING
        ivPlayState.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_pause))
    }

    override fun onTick(duration: Long) = with(binding) {
        val millisecond = (duration % 1000) / 10 // 41.2ms → 41ms
        val second = (duration / 1000) % 60
        val minute = (duration / 1000) / 60
        tvRecordDuration.text = String.format("%02d:%02d.%02d", minute, second, millisecond)
        viewVoiceWaveForm.addAmplitude(recorder?.maxAmplitude?.toFloat() ?: 0f)
    }

    companion object {
        const val TAG = "RECORD_BOTTOM_SHEET_DIALOG"
    }

    private enum class RecordState {
        RELEASE, RECORDING
    }

    private enum class PlayState {
        RELEASE, PLAYING, PAUSED
    }
}