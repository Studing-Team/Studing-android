package com.team.studing.UI.Notice

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.team.studing.API.response.Home.NoticeDetailResponse
import com.team.studing.MainActivity
import com.team.studing.R
import com.team.studing.RegisterNoticeActivity
import com.team.studing.UI.Notice.Adapter.NoticeImagePagerAdapter
import com.team.studing.Utils.BasicToast
import com.team.studing.Utils.GlobalApplication.Companion.amplitude
import com.team.studing.Utils.MyApplication
import com.team.studing.ViewModel.NoticeViewModel
import com.team.studing.databinding.FragmentNoticeDetailBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class NoticeDetailFragment : Fragment() {

    lateinit var binding: FragmentNoticeDetailBinding
    lateinit var mainActivity: MainActivity

    private val viewModel: NoticeViewModel by lazy {
        ViewModelProvider(mainActivity)[NoticeViewModel::class.java]
    }

    var isLike: Boolean? = false
    var isScrap: Boolean? = false
    var isView: Boolean? = false

    var firstEventStatus: FirstEventState = FirstEventState.BEFORE

    private var getNoticeDetail: NoticeDetailResponse? = null
    private lateinit var noticeImageAdapter: NoticeImagePagerAdapter

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentNoticeDetailBinding.inflate(layoutInflater)
        mainActivity = activity as MainActivity

        observeViewModel()

        MyApplication.notificationNoticeType = ""
        MyApplication.notificationNoticeId = ""

        binding.run {

            noticeImageAdapter = NoticeImagePagerAdapter(
                mainActivity,
                getNoticeDetail?.images
            )

            buttonScrap.setOnClickListener {
                amplitude.track("click_save_post_detail")

                if (!isScrap!!) {
                    viewModel.scrapNotice(mainActivity, getNoticeDetail?.id!!)
                } else {
                    viewModel.cancelScrapNotice(mainActivity, getNoticeDetail?.id!!)
                }
            }

            buttonLikeNotice.setOnClickListener {
                amplitude.track("click_like_post_detail")

                if (!isLike!!) {
                    viewModel.likeNotice(mainActivity, getNoticeDetail?.id!!)
                } else {
                    viewModel.cancelLikeNotice(mainActivity, getNoticeDetail?.id!!)
                }
            }

            layoutNoticeDetail.buttonFirstEvent.setOnClickListener {
                when (firstEventStatus) {
                    FirstEventState.ACTIVE -> {
                        // 선착순 이벤트 참여
                        viewModel.joinFirstEvent(
                            mainActivity,
                            getNoticeDetail?.id!!,
                            "detail"
                        )
                    }

                    FirstEventState.COMPLETE -> {
                        // 내 순위 확인
                        viewModel.getFirstEventResult(mainActivity, getNoticeDetail?.id!!)
                    }

                    else -> {}
                }
            }
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        initView()
        viewModel.getNoticeDetail(
            mainActivity,
            MyApplication.noticeId,
            false
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModelStore.clear() // Fragment 전체 소멸 시 초기화
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModelStore.clear()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun observeViewModel() {
        viewModel.run {
            noticeDetail.observe(viewLifecycleOwner) {
                getNoticeDetail = it
                Log.d("##", "notice detail update : ${getNoticeDetail}")
                setData()
            }

            isLiked.observe(viewLifecycleOwner) {
                isLike = it
                binding.run {
                    if (isLike == true) {
                        getNoticeDetail?.likeCount = getNoticeDetail?.likeCount!! + 1
                        buttonLikeNotice.setImageResource(R.drawable.ic_like_primary50)
                        layoutNoticeDetail.imageViewLike.setImageResource(R.drawable.ic_like_red)
                        layoutNoticeDetail.textViewLikeNum.text =
                            "${getNoticeDetail?.likeCount!!}"
                    } else {
                        getNoticeDetail?.likeCount = getNoticeDetail?.likeCount!! - 1
                        buttonLikeNotice.setImageResource(R.drawable.ic_like_primary20)
                        layoutNoticeDetail.imageViewLike.setImageResource(R.drawable.ic_like_black30)
                        layoutNoticeDetail.textViewLikeNum.text =
                            "${getNoticeDetail?.likeCount!!}"
                    }
                }
            }

            isScraped.observe(viewLifecycleOwner) {
                isScrap = it
                binding.run {
                    if (isScrap == true) {
                        getNoticeDetail?.saveCount = getNoticeDetail?.saveCount!! + 1
                        layoutNoticeDetail.imageViewScrap.setImageResource(R.drawable.ic_scrap_selected)
                        BasicToast.createToastAboveView(
                            requireContext(),
                            "저장한 공지사항에 추가했어요 ⭐",
                            binding.buttonScrap
                        )
                        buttonScrap.text = "저장 취소"
                    } else {
                        getNoticeDetail?.saveCount = getNoticeDetail?.saveCount!! - 1
                        layoutNoticeDetail.imageViewScrap.setImageResource(R.drawable.ic_scrap_black30)
                        BasicToast.createToastAboveView(
                            requireContext(),
                            "저장한 공지사항을 취소했어요 ⭐",
                            binding.buttonScrap
                        )
                        buttonScrap.text = "저장하기"
                    }
                    layoutNoticeDetail.textViewScrapNum.text = "${getNoticeDetail?.saveCount!!}"
                }
            }

            isViewed.observe(viewLifecycleOwner) {
                isView = it
                if (isView == true) {
                    binding.layoutNoticeDetail.textViewShowNum.text =
                        "${getNoticeDetail?.readCount!! + 1}"
                } else {
                    binding.layoutNoticeDetail.textViewShowNum.text =
                        getNoticeDetail?.readCount.toString()
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun setData() {
        binding.run {
            layoutNoticeDetail.run {
                if (getNoticeDetail?.startTime != null && getNoticeDetail?.endTime != null) {
                    layoutEventTime.visibility = View.VISIBLE
                    textViewEventStartTimeValue.text =
                        changDateToString(getNoticeDetail?.startTime!!)
                    textViewEventEndTimeValue.text = changDateToString(getNoticeDetail?.endTime!!)
                } else {
                    layoutEventTime.visibility = View.GONE
                }

                if (getNoticeDetail?.isFirstComeNotice == true) {
                    // 선착순 이벤트인 경우
                    buttonFirstEvent.visibility = View.VISIBLE
                    textViewEventTimeTitle.text = "선착순 이벤트 안내"
                    textViewEventNum.text = "인원 ${getNoticeDetail?.firstComeNumber}명"
                    checkNoticeTime(
                        getNoticeDetail?.startTime!!,
                        getNoticeDetail?.endTime!!
                    )
                } else {
                    buttonFirstEvent.visibility = View.GONE
                    textViewEventTimeTitle.text = "공지사항 안내"
                }

                if (getNoticeDetail?.alarmTime != null) {
                    toolbar.buttonNotification.setImageResource(R.drawable.ic_notification_selected)
                } else {
                    toolbar.buttonNotification.setImageResource(R.drawable.ic_notification_unselected)
                }

                textViewNoticeTitle.text = getNoticeDetail?.title
                textViewNoticeContent.text = getNoticeDetail?.content
                textViewLikeNum.text = getNoticeDetail?.likeCount.toString()
                isLike = getNoticeDetail?.likeCheck!!
                if (getNoticeDetail?.likeCheck == true) {
                    imageViewLike.setImageResource(R.drawable.ic_like_red)
                    buttonLikeNotice.setImageResource(R.drawable.ic_like_primary50)
                } else {
                    imageViewLike.setImageResource(R.drawable.ic_like_black30)
                    buttonLikeNotice.setImageResource(R.drawable.ic_like_primary20)
                }
                textViewScrapNum.text = getNoticeDetail?.saveCount.toString()
                isScrap = getNoticeDetail?.saveCheck!!
                if (getNoticeDetail?.saveCheck == true) {
                    imageViewScrap.setImageResource(R.drawable.ic_scrap_selected)
                    buttonScrap.text = "저장 취소"
                } else {
                    imageViewScrap.setImageResource(R.drawable.ic_scrap_black30)
                    buttonScrap.text = "저장하기"
                }
                textViewShowNum.text = getNoticeDetail?.readCount.toString()
                textViewDate.text = getNoticeDetail?.createdAt
                textViewStudentCouncil.text = getNoticeDetail?.affilitionName
                Glide.with(mainActivity).load(getNoticeDetail?.logoImage)
                    .into(imageViewStudentCouncil)

                chipNoticeType.text = getNoticeDetail?.tag
                when (getNoticeDetail?.tag) {
                    "공지" -> {
                        chipNoticeType.run {
                            setBackgroundResource(R.drawable.background_notice_type_chip_primary20)
                            setTextColor(resources.getColor(R.color.primary_50))
                        }
                    }

                    "이벤트" -> {
                        chipNoticeType.run {
                            setBackgroundResource(R.drawable.background_notice_type_chip_red)
                            setTextColor(resources.getColor(R.color.red))
                        }
                    }

                    "선착순" -> {
                        chipNoticeType.run {
                            setBackgroundResource(R.drawable.background_notice_type_chip_red)
                            setTextColor(resources.getColor(R.color.red))
                        }
                    }
                }

                if (getNoticeDetail?.isAuthor == true) {
                    toolbar.buttonKebabMenu.visibility = View.VISIBLE
                } else {
                    toolbar.buttonKebabMenu.visibility = View.GONE
                }

                if (getNoticeDetail?.images.isNullOrEmpty()) {
                    viewPager.visibility = View.GONE
                    viewPager.isUserInputEnabled = true // 슬라이드 동작 활성화
                    dotsIndicatorNotice.visibility = View.GONE
                } else {
                    viewPager.visibility = View.VISIBLE
                    if (getNoticeDetail?.images?.size == 1) {
                        dotsIndicatorNotice.visibility = View.GONE
                        viewPager.isUserInputEnabled = false // 슬라이드 동작 비활성화
                    } else {
                        dotsIndicatorNotice.visibility = View.VISIBLE
                        viewPager.isUserInputEnabled = true // 슬라이드 동작 활성화
                    }
                    noticeImageAdapter.updateList(getNoticeDetail?.images)
                    viewPager.apply {
                        adapter = noticeImageAdapter
                        dotsIndicatorNotice.attachTo(this)
                    }
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun checkNoticeTime(startTime: String, endTime: String) {
        // 날짜 및 시간 설정
        val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

        // LocalDateTime으로 변환
        val startLocalTime = LocalDateTime.parse(startTime, formatter)
        val endLocalTime = LocalDateTime.parse(endTime, formatter)
        val currentTime = LocalDateTime.now()

        // 현재 시간이 시작-종료 범위 내에 있는지 확인
        val isWithinTimeRange =
            currentTime.isAfter(startLocalTime) && currentTime.isBefore(endLocalTime)

        Log.d("##", "$startLocalTime , $endLocalTime , $currentTime : $isWithinTimeRange")

        // 현재 시간이 범위 내에 있는지 확인하여 버튼 상태 결정
        firstEventStatus =
            if (getNoticeDetail?.isFirstComeApplied == true) {
                FirstEventState.COMPLETE
            } else {
                if (currentTime.isAfter(startLocalTime) && currentTime.isBefore(endLocalTime)) {
                    FirstEventState.ACTIVE
                } else if (currentTime.isBefore(startLocalTime)) {
                    FirstEventState.BEFORE
                } else {
                    FirstEventState.FINISH
                }
            }

        // 버튼 UI 업데이트
        binding.layoutNoticeDetail.buttonFirstEvent.run {
            text = firstEventStatus.text
            backgroundTintList =
                ContextCompat.getColorStateList(mainActivity, firstEventStatus.colorId)
            isEnabled = firstEventStatus.isEnabled
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun changDateToString(time: String): String? {
        // LocalDateTime으로 변환
        val localDateTime = LocalDateTime.parse(time)

        // 변환할 포맷 설정
        val formatter = DateTimeFormatter.ofPattern("yyyy년 M월 d일 HH시 mm분", Locale.KOREAN)

        // LocalDateTime을 문자열로 변환
        val formattedTime = localDateTime.format(formatter)

        return formattedTime
    }


    fun initView() {

        mainActivity.hideBottomNavigation(true)
        mainActivity.hideWriteNoticeButton(true)

        MyApplication.notificationNoticeType = ""
        MyApplication.notificationNoticeId = ""

        binding.run {

            toolbar.run {
                buttonBack.setOnClickListener {
                    fragmentManager?.popBackStack()
                }
                textViewTitle.text = "공지사항"

                buttonNotification.setOnClickListener {
                    val dialog = DialogRemindNotification(mainActivity, getNoticeDetail?.alarmTime)

                    dialog.setNoticeDeleteDialogInterface(object :
                        RemindNotificationDialogInterface {
                        override fun onClickSettingButton(date: String, time: String) {
                            // 공지사항 리마인드 알림 설정 기능 구현
                            viewModel.setRemindNotification(
                                mainActivity,
                                getNoticeDetail?.id!!,
                                date,
                                time,
                                "detail"
                            )
                        }

                        override fun onClickUnSettingButton() {
                            // 공지사항 리마인드 알림 취소 기능 구현
                            viewModel.deleteRemindNotification(
                                mainActivity,
                                getNoticeDetail?.id!!,
                                "detail"
                            )
                        }
                    })

                    dialog.show(parentFragmentManager, "DialogNoticeRemindNotification")
                }

                buttonKebabMenu.setOnClickListener {
//                    showPopUpMenu()
                    val popupView =
                        LayoutInflater.from(context).inflate(R.layout.popup_menu_item, null)

                    val popupWindow = PopupWindow(
                        popupView,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        true
                    )
                    popupWindow.elevation = 50f

                    // 팝업 클릭 이벤트 설정
                    popupView.findViewById<TextView>(R.id.textView_menu_edit).setOnClickListener {

                        val mainIntent =
                            Intent(mainActivity, RegisterNoticeActivity::class.java)
                        when (getNoticeDetail?.tag) {
                            "선착순" -> {
                                mainIntent.run {
                                    putExtra("type", "first event")
                                    putExtra("isEdit", true)
                                    putExtra("id", getNoticeDetail?.id!!)
                                    putExtra("title", "${getNoticeDetail?.title}")
                                    putExtra("content", "${getNoticeDetail?.content}")
                                    putExtra("tag", "${getNoticeDetail?.tag}")
                                    putExtra("image", ArrayList(getNoticeDetail?.images))
                                    putExtra("firstEventNumber", getNoticeDetail?.firstComeNumber)
                                    putExtra("startTime", getNoticeDetail?.startTime)
                                    putExtra("endTime", getNoticeDetail?.endTime)
                                }
                            }

                            else -> {
                                mainIntent.run {
                                    putExtra("type", "notice")
                                    putExtra("isEdit", true)
                                    putExtra("id", getNoticeDetail?.id!!)
                                    putExtra("title", "${getNoticeDetail?.title}")
                                    putExtra("content", "${getNoticeDetail?.content}")
                                    putExtra("tag", "${getNoticeDetail?.tag}")
                                    putExtra("image", ArrayList(getNoticeDetail?.images))
                                    putExtra("startTime", getNoticeDetail?.startTime)
                                    putExtra("endTime", getNoticeDetail?.endTime)
                                }
                            }
                        }
                        startActivity(mainIntent)

                        popupWindow.dismiss()
                    }

                    popupView.findViewById<TextView>(R.id.textView_menu_delete).setOnClickListener {
                        // 공지사항 삭제 dialog
                        val dialog = DialogNoticeDelete()

                        dialog.setNoticeDeleteDialogInterface(object : NoticeDeleteDialogInterface {
                            override fun onClickYesButton() {
                                // 공지사항 삭제 기능 구현
                                viewModel.deleteNotice(mainActivity, getNoticeDetail?.id!!)
                            }
                        })

                        dialog.show(parentFragmentManager, "DialogNoticeDelete")
                        popupWindow.dismiss()
                    }

                    // 팝업 표시
                    popupWindow.showAsDropDown(buttonKebabMenu, -330, 20)
                }
            }
        }
    }
}
