package com.team.studing.UI.Mypage

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.team.studing.databinding.DialogMypageBinding

interface WithdrawalDialogInterface {
    fun onClickYesButton()
}

class DialogWithdrawal : DialogFragment() {

    // 뷰 바인딩 정의
    private var _binding: DialogMypageBinding? = null
    private val binding get() = _binding!!

    private var confirmDialogInterface: WithdrawalDialogInterface? = null

    // 인터페이스 인스턴스
    private var listener: WithdrawalDialogInterface? = null

    // 리스너 설정 메서드
    fun setWithdrawalDialogInterface(listener: WithdrawalDialogInterface) {
        this.listener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogMypageBinding.inflate(inflater)

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)


        binding.buttonCancel.setOnClickListener {
            listener?.onClickYesButton() // 인터페이스를 통해 이벤트 전달
            dismiss()
        }

        binding.buttonAccept.setOnClickListener {
            dismiss()
        }

        // 다이얼로그 텍스트 설정
        binding.run {
            textViewTitle.text = "잠깐만요"
            textViewDescription.text = "모든 안내사항을 확인해주세요.\n탈퇴를 진행할까요?"
            buttonAccept.text = "아니요"
            buttonCancel.text = "탈퇴하기"
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