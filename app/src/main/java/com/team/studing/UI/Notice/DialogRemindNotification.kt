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
import com.team.studing.MainActivity
import com.team.studing.UI.Register.DateBottomSheetFragment
import com.team.studing.UI.Register.DateBottomSheetInterface
import com.team.studing.UI.Register.TimeBottomSheetFragment
import com.team.studing.UI.Register.TimeBottomSheetInterface
import com.team.studing.databinding.DialogRemindNotificationBinding

interface RemindNotificationDialogInterface {
    fun onClickYesButton()
}

class DialogRemindNotification(var activity: MainActivity) : DialogFragment() {

    // 뷰 바인딩 정의
    private var _binding: DialogRemindNotificationBinding? = null
    private val binding get() = _binding!!

    private var confirmDialogInterface: RemindNotificationDialogInterface? = null

    // 인터페이스 인스턴스
    private var listener: RemindNotificationDialogInterface? = null

    // 리스너 설정 메서드
    fun setNoticeDeleteDialogInterface(listener: RemindNotificationDialogInterface) {
        this.listener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogRemindNotificationBinding.inflate(inflater)

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)


        binding.run {

            editTextDate.setOnClickListener {
                // 캘린더 bottom sheet
                val dateBottomsheet = DateBottomSheetFragment()

                dateBottomsheet.setDateBottomSheetInterface(object : DateBottomSheetInterface {
                    override fun onDateClickCompleteButton(date: String) {
                        editTextDate.setText(date)
                    }
                })

                dateBottomsheet.show(
                    activity.supportFragmentManager,
                    "DateBottomsheet"
                )
            }

            editTextTime.setOnClickListener {
                // 시간 설정 bottom sheet
                val timeBottomsheet = TimeBottomSheetFragment()

                timeBottomsheet.setTimeBottomSheetInterface(object : TimeBottomSheetInterface {
                    override fun onTimeClickCompleteButton(time: String) {
                        editTextTime.setText(time)
                    }
                })

                timeBottomsheet.show(
                    activity.supportFragmentManager,
                    "TimeBottomsheet"
                )
            }

            buttonCancel.setOnClickListener {
                // 공지사항 삭제
                listener?.onClickYesButton() // 인터페이스를 통해 이벤트 전달
                dismiss()
            }

            buttonSetting.setOnClickListener {
                // 리마인드 푸시 알림 설정
                dismiss()
            }
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