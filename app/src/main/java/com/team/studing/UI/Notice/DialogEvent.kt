package com.team.studing.UI.Notice

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.team.studing.databinding.DialogEventBinding

interface EventDialogInterface {
    fun onClickYesButton()
}

class DialogEvent : DialogFragment() {

    // 뷰 바인딩 정의
    private var _binding: DialogEventBinding? = null
    private val binding get() = _binding!!

    private var confirmDialogInterface: EventDialogInterface? = null

    // 인터페이스 인스턴스
    private var listener: EventDialogInterface? = null

    // 리스너 설정 메서드
    fun setEventDialogInterface(listener: EventDialogInterface) {
        this.listener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogEventBinding.inflate(inflater)

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)

        binding.buttonAccept.setOnClickListener {
            listener?.onClickYesButton()
            dismiss()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }
}