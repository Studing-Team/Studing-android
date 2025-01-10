package com.team.studing.UI.Notice

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
                    unreadNoticePagerAdapter.updateList(getUnreadNotices)
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
                unreadNoticePagerAdapter.updateList(getUnreadNotices)
            }

            isViewed.observe(viewLifecycleOwner) {
                isView = it
                if (isView == true) {
                    getUnreadNotices[currentItem].readCount += 1
                }
                unreadNoticePagerAdapter.updateList(getUnreadNotices)
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

        binding.run {

            toolbar.run {
                buttonBack.setOnClickListener {
                    amplitude.track("click_back_unread")
                    // 홈화면으로 이동
                    fragmentManager?.popBackStack()
                }
            }
        }
    }
}