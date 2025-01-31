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
import com.team.studing.databinding.FragmentRegisterNoticeBottomSheetBinding

interface RegisterNoticeBottomSheetInterface {
    fun onClickFirstEventButton(id: Int)
    fun onClickNoticeButton(id: Int)
}

class RegisterNoticeBottomSheetFragment : DialogFragment() {

    lateinit var binding: FragmentRegisterNoticeBottomSheetBinding

    // 인터페이스 인스턴스
    private var listener: RegisterNoticeBottomSheetInterface? = null

    // 리스너 설정 메서드
    fun setLogoutDialogInterface(listener: RegisterNoticeBottomSheetInterface) {
        this.listener = listener
    }

    override fun onStart() {
        super.onStart()

        // 다이얼로그를 하단에 배치하고 마진 설정
        dialog?.window?.let { window ->
            val params = window.attributes

            // 화면 너비에서 좌우 마진과 하단 마진 계산
            val marginHorizontalInPx =
                (20 * requireContext().resources.displayMetrics.density).toInt() // 좌우 마진 20dp
            val marginBottomInPx =
                (11 * requireContext().resources.displayMetrics.density).toInt() // 하단 마진 11dp

            params.width =
                requireContext().resources.displayMetrics.widthPixels - (marginHorizontalInPx * 2) // 좌우 마진 적용
            params.height = WindowManager.LayoutParams.WRAP_CONTENT // 세로는 콘텐츠 크기
            params.gravity = android.view.Gravity.BOTTOM // 하단에 배치
            params.y = marginBottomInPx // 하단 마진 적용

            window.attributes = params
            window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // 배경 투명
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterNoticeBottomSheetBinding.inflate(inflater, container, false)

        // 배경을 투명하게 설정
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)

        // 클릭 이벤트 처리
        binding.run {
            layoutFirstEvent.setOnClickListener {
                listener?.onClickFirstEventButton(0)
                dismiss()
            }
            layoutNotice.setOnClickListener {
                listener?.onClickNoticeButton(0)
                dismiss()
            }
            buttonClose.setOnClickListener {
                dismiss()
            }
        }

        return binding.root
    }
}
