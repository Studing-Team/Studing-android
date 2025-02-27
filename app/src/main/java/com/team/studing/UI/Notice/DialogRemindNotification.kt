package com.team.studing.UI.Notice

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import com.team.studing.MainActivity
import com.team.studing.UI.Register.DateBottomSheetFragment
import com.team.studing.UI.Register.DateBottomSheetInterface
import com.team.studing.UI.Register.TimeBottomSheetFragment
import com.team.studing.UI.Register.TimeBottomSheetInterface
import com.team.studing.databinding.DialogRemindNotificationBinding
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

interface RemindNotificationDialogInterface {
    fun onClickSettingButton(date: String, time: String)
    fun onClickUnSettingButton()
}

class DialogRemindNotification(var activity: MainActivity, var inputTime: String?) :
    DialogFragment() {

    // 뷰 바인딩 정의
    private var _binding: DialogRemindNotificationBinding? = null
    private val binding get() = _binding!!

    private var confirmDialogInterface: RemindNotificationDialogInterface? = null

    // 인터페이스 인스턴스
    private var listener: RemindNotificationDialogInterface? = null

    var isRemindNotification = inputTime != null

    // 리스너 설정 메서드
    fun setNoticeDeleteDialogInterface(listener: RemindNotificationDialogInterface) {
        this.listener = listener
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogRemindNotificationBinding.inflate(inflater)

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)


        binding.run {
            // 다이얼로그 텍스트 설정
            if (isRemindNotification) {
                textViewTitle.text = "리마인드 알림 취소"
                textViewDescription.text = "해당 공지사항에 설정한\n푸쉬알림을 취소하시겠습니까?"
                buttonSetting.text = "네"
                buttonCancel.text = "아니요"

                var (date, time) = splitDateTimeString(inputTime!!)

                // 알람 시간 텍스트 설정
                editTextDate.run {
                    setText(date)
                    isFocusable = false
                    isClickable = false
                    isEnabled = false
                }
                editTextTime.run {
                    setText(time)
                    isFocusable = false
                    isClickable = false
                    isEnabled = false
                }
            } else {
                textViewTitle.text = "알림 설정"
                textViewDescription.text = "리마인드 푸쉬 알림을 받고 싶은\n날짜와 시간을 설정해주세요!"
                buttonSetting.text = "확인"
                buttonCancel.text = "취소"
            }

            editTextDate.setOnClickListener {
                // 캘린더 bottom sheet
                val dateBottomsheet = if (editTextDate.text.isNotEmpty()) {
                    DateBottomSheetFragment(getTodayDateString(), editTextDate.text.toString())
                } else {
                    DateBottomSheetFragment(getTodayDateString(), getTodayDateString())
                }

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
                dismiss()
            }

            buttonSetting.setOnClickListener {
                if (!isRemindNotification) {
                    // 리마인드 푸시 알림 설정
                    listener?.onClickSettingButton(
                        editTextDate.text.toString(),
                        editTextTime.text.toString()
                    )
                } else {
                    // 리마인드 푸시 알림 취소
                    listener?.onClickUnSettingButton()
                }
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

    fun getTodayDateString(): String {
        val calendar = Calendar.getInstance() // 현재 날짜 가져오기
        val dateFormat = SimpleDateFormat("yyyy년 MM월 dd일", Locale.KOREA)
        return dateFormat.format(calendar.time) // 날짜를 원하는 형식으로 변환
    }

    // 문자열을 받아 날짜와 시간을 분리하는 함수
    @RequiresApi(Build.VERSION_CODES.O)
    fun splitDateTimeString(dateTimeStr: String): Pair<String, String> {
        // 문자열을 LocalDateTime으로 파싱
        val dateTime = LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME)

        // 날짜 포맷 (2025년 2월 5일)
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy년 M월 d일", Locale.KOREAN)
        val dateString = dateTime.toLocalDate().format(dateFormatter)

        // 시간 포맷 (13:00)
        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm", Locale.KOREAN)
        val timeString = dateTime.toLocalTime().format(timeFormatter)

        return Pair(dateString, timeString)
    }
}