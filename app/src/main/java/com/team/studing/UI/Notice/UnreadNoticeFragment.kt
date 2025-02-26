package com.team.studing.UI.Notice

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.team.studing.API.response.Home.UnreadNotice
import com.team.studing.MainActivity
import com.team.studing.R
import com.team.studing.UI.Notice.Adapter.UnreadNoticePagerAdapter
import com.team.studing.Utils.BasicToast
import com.team.studing.Utils.GlobalApplication.Companion.amplitude
import com.team.studing.ViewModel.HomeViewModel
import com.team.studing.ViewModel.NoticeViewModel
import com.team.studing.databinding.FragmentUnreadNoticeBinding

class UnreadNoticeFragment : Fragment() {

    lateinit var binding: FragmentUnreadNoticeBinding
    lateinit var mainActivity: MainActivity
    lateinit var viewModel: HomeViewModel
    lateinit var noticeViewModel: NoticeViewModel
    lateinit var unreadNoticePagerAdapter: UnreadNoticePagerAdapter

    var isScrap = false
    var isLike = false
    var isView = false

    var currentItem = 0

    var getUnreadNotices = mutableListOf<UnreadNotice>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentUnreadNoticeBinding.inflate(layoutInflater)
        mainActivity = activity as MainActivity
        viewModel = ViewModelProvider(mainActivity)[HomeViewModel::class.java]
        noticeViewModel = ViewModelProvider(mainActivity)[NoticeViewModel::class.java]

        viewModelObserver()

        binding.run {
            buttonLikeNotice.setOnClickListener {
                amplitude.track("click_like_post_unread")
                isLike = getUnreadNotices[currentItem].likeCheck
                if (!isLike) {
                    noticeViewModel.likeNotice(mainActivity, getUnreadNotices[currentItem].id)
                } else {
                    noticeViewModel.cancelLikeNotice(mainActivity, getUnreadNotices[currentItem].id)
                }
            }

            buttonScrap.setOnClickListener {
                amplitude.track("click_save_post_unread")
                isScrap = getUnreadNotices[currentItem].saveCheck
                if (!isScrap) {
                    noticeViewModel.scrapNotice(mainActivity, getUnreadNotices[currentItem].id)
                } else {
                    noticeViewModel.cancelScrapNotice(
                        mainActivity,
                        getUnreadNotices[currentItem].id
                    )
                }
            }

            buttonNext.setOnClickListener {
                amplitude.track("click_next_notice_unread")
                // 다음 공지사항으로 이동
                var previousItem = binding.viewPager2.currentItem
                currentItem = binding.viewPager2.currentItem + 1
                val totalItems = unreadNoticePagerAdapter.itemCount

                toolbar.textViewTitle.text = "${totalItems - currentItem}개 남음"
                if (currentItem <= totalItems - 1) {
                    // 현재 페이지가 마지막 페이지가 아니면 다음 페이지로 이동
                    noticeViewModel.viewNotice(mainActivity, getUnreadNotices[currentItem].id)
                    viewPager2.setCurrentItem(currentItem, true)

                    binding.run {
                        if (getUnreadNotices[currentItem].likeCheck) {
                            buttonLikeNotice.setImageResource(R.drawable.ic_like_primary50)
                        } else {
                            buttonLikeNotice.setImageResource(R.drawable.ic_like_primary20)
                        }

                        if (getUnreadNotices[currentItem].saveCheck) {
                            buttonScrap.text = "저장 취소"
                        } else {
                            buttonScrap.text = "저장하기"
                        }
                    }
                } else {
                    // 마지막 페이지면 버튼을 비활성화하거나 다른 작업 수행
                    mainActivity.supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerView_main, UnreadNoticeFinishFragment())
                        .addToBackStack(null)
                        .commit()
                }
            }
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        initView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModelStore.clear()
    }

    fun viewModelObserver() {
        viewModel.unreadNoticeList.observe(viewLifecycleOwner) { unreadNotices ->
            getUnreadNotices = unreadNotices.toMutableList()
            currentItem = 0 // 초기화
            binding.toolbar.textViewTitle.text = "${getUnreadNotices.size}개 남음"

            unreadNoticePagerAdapter = UnreadNoticePagerAdapter(mainActivity, getUnreadNotices)

            binding.viewPager2.run {
                adapter = unreadNoticePagerAdapter
                isUserInputEnabled = false // 슬라이드 동작 비활성화

                // ViewPager 어댑터가 설정되고 첫 페이지가 표시되었을 때 조회수 업데이트
                post {
                    if (getUnreadNotices.isNotEmpty()) {
                        noticeViewModel.viewNotice(mainActivity, getUnreadNotices[currentItem].id)
                    }
                }
            }
        }

        noticeViewModel.run {
            isLiked.observe(viewLifecycleOwner) {
                isLike = it
                binding.run {
                    if (isLike == true) {
                        getUnreadNotices[currentItem].likeCount += 1
                        buttonLikeNotice.setImageResource(R.drawable.ic_like_primary50)
                        getUnreadNotices[currentItem].likeCheck = true
                    } else {
                        getUnreadNotices[currentItem].likeCount -= 1
                        buttonLikeNotice.setImageResource(R.drawable.ic_like_primary20)
                        getUnreadNotices[currentItem].likeCheck = false
                    }
                    unreadNoticePagerAdapter.updateList(getUnreadNotices, null)
                }
            }

            isScraped.observe(viewLifecycleOwner) {
                isScrap = it
                binding.run {
                    if (isScrap == true) {
                        getUnreadNotices[currentItem].saveCount += 1
                        getUnreadNotices[currentItem].saveCheck = true
                        BasicToast.createToastAboveView(
                            requireContext(),
                            "저장한 공지사항에 추가했어요 ⭐",
                            binding.buttonScrap
                        )
                        buttonScrap.text = "저장 취소"
                    } else {
                        getUnreadNotices[currentItem].saveCount -= 1
                        getUnreadNotices[currentItem].saveCheck = false
                        BasicToast.createToastAboveView(
                            requireContext(),
                            "저장한 공지사항을 취소했어요 ⭐",
                            binding.buttonScrap
                        )
                        buttonScrap.text = "저장하기"
                    }
                }
                unreadNoticePagerAdapter.updateList(getUnreadNotices, null)
            }

            isViewed.observe(viewLifecycleOwner) {
                isView = it
                if (isView == true) {
                    getUnreadNotices[currentItem].readCount += 1
                }
                unreadNoticePagerAdapter.updateList(getUnreadNotices, null)

                if (getUnreadNotices[currentItem].isAuthor) {
                    binding.toolbar.buttonKebabMenu.visibility = View.VISIBLE
                } else {
                    binding.toolbar.buttonKebabMenu.visibility = View.GONE
                }

                if (getUnreadNotices[currentItem].alarmTime != null) {
                    binding.toolbar.buttonNotification.setImageResource(R.drawable.ic_notification_selected)
                } else {
                    binding.toolbar.buttonNotification.setImageResource(R.drawable.ic_notification_unselected)
                }
            }

            updateNoticeDetail.observe(viewLifecycleOwner) {
                if (it?.alarmTime != null) {
                    getUnreadNotices[currentItem].alarmTime = it.alarmTime
                    binding.toolbar.buttonNotification.setImageResource(R.drawable.ic_notification_selected)
                } else {
                    getUnreadNotices[currentItem].alarmTime = it?.alarmTime
                    binding.toolbar.buttonNotification.setImageResource(R.drawable.ic_notification_unselected)
                }
                Log.d("##", "alarm time : ${getUnreadNotices[currentItem].alarmTime}")
            }
        }
    }

    fun initView() {

        mainActivity.hideBottomNavigation(true)
        mainActivity.hideWriteNoticeButton(true)

        viewModel.getUnreadNoticeList(
            mainActivity,
            "전체"
        )

        binding.toolbar.run {
            buttonNotification.setOnClickListener {
                val dialog =
                    DialogRemindNotification(mainActivity, getUnreadNotices[currentItem].alarmTime)

                dialog.setNoticeDeleteDialogInterface(object :
                    RemindNotificationDialogInterface {
                    override fun onClickSettingButton(date: String, time: String) {
                        // 공지사항 리마인드 알림 설정 기능 구현
                        noticeViewModel.setRemindNotification(
                            mainActivity,
                            getUnreadNotices[currentItem].id,
                            date,
                            time,
                            "unread"
                        )
                    }

                    override fun onClickUnSettingButton() {
                        // 공지사항 리마인드 알림 취소 기능 구현
                        noticeViewModel.deleteRemindNotification(
                            mainActivity,
                            getUnreadNotices[currentItem].id,
                            "unread"
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
                    // 데이터 전달을 위해 Bundle 생성
                    val bundle = Bundle().apply {
                        putInt("id", getUnreadNotices[currentItem].id)
                        putString("title", "${getUnreadNotices[currentItem].title}")
                        putString("content", "${getUnreadNotices[currentItem].content}")
                        putString("tag", "${getUnreadNotices[currentItem].tag}")
                        putString("startTime", "${getUnreadNotices[currentItem].startTime}")
                        putString("endTime", "${getUnreadNotices[currentItem].endTime}")
                        putStringArrayList(
                            "image",
                            ArrayList(getUnreadNotices[currentItem].images)
                        )
                    }

                    // 전달할 Fragment 생성
                    val noticeEditFragment = NoticeEditFragment().apply {
                        arguments = bundle // 생성한 Bundle을 Fragment의 arguments에 설정
                    }

                    // Fragment 전환
                    mainActivity.supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerView_main, noticeEditFragment)
                        .addToBackStack(null)
                        .commit()

                    popupWindow.dismiss()
                }

                popupView.findViewById<TextView>(R.id.textView_menu_delete).setOnClickListener {
                    // 공지사항 삭제 dialog
                    val dialog = DialogNoticeDelete()

                    dialog.setNoticeDeleteDialogInterface(object : NoticeDeleteDialogInterface {
                        override fun onClickYesButton() {
                            // 공지사항 삭제 기능 구현
                            noticeViewModel.deleteNotice(
                                mainActivity,
                                getUnreadNotices[currentItem].id
                            )
                        }
                    })

                    dialog.show(parentFragmentManager, "DialogNoticeDelete")
                    popupWindow.dismiss()
                }

                // 팝업 표시
                popupWindow.showAsDropDown(buttonKebabMenu, -330, 20)
            }

            buttonBack.setOnClickListener {
                amplitude.track("click_back_unread")
                // 홈화면으로 이동
                fragmentManager?.popBackStack()
            }
        }
    }
}