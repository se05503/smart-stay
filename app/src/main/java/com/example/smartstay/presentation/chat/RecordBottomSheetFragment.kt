package com.example.smartstay.presentation.chat

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.example.smartstay.R
import com.example.smartstay.databinding.BottomSheetRecordBinding

class RecordBottomSheetFragment: BottomSheetDialogFragment(R.layout.bottom_sheet_record) {

    private lateinit var binding: BottomSheetRecordBinding
    private var recordState: State = State.RELEASE

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = BottomSheetRecordBinding.bind(view)
        initViews()
        initListeners()
    }

    private fun initViews() = with(binding) {
        sivSendVoiceMessage.isEnabled = false
        sivSendVoiceMessage.alpha = 0.3f
    }

    private fun initListeners() = with(binding) {
        tvBackToChat.setOnClickListener {
            dismiss()
        }
        sivRecordVoiceState.setOnClickListener {
            when(recordState) {
                State.RELEASE -> {
                    // TODO: 녹음 시작하기
                    startRecording()
                }
                State.RECORDING -> {
                    // TODO: 녹음 중지 및 저장하기
                    stopRecording()
                }
                State.PLAYING -> {}
            }
        }
    }
    // 녹음을 시작하는 메소드
    private fun startRecording() = with(binding) {

        recordState = State.RECORDING
        sivRecordVoiceState.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_stop_record))
        sivSendVoiceMessage.isEnabled = false
        sivSendVoiceMessage.alpha = 0.3f
        ivPlayRecord.isVisible = false

    }
    // 녹음을 중지하고 저장하는 메소드
    private fun stopRecording() = with(binding) {
        recordState = State.RELEASE
        sivRecordVoiceState.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_record))
        sivSendVoiceMessage.isEnabled = true
        sivSendVoiceMessage.alpha = 1f
        ivPlayRecord.isVisible = true
    }

    companion object {
        const val TAG = "RECORD_BOTTOM_SHEET_DIALOG"
    }

    private enum class State {
        RELEASE, RECORDING, PLAYING
    }
}