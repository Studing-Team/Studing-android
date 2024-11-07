package com.team.studing.Home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.team.studing.MainActivity
import com.team.studing.R
import com.team.studing.Utils.BasicToast
import com.team.studing.databinding.FragmentUnreadNoticeBinding

class UnreadNoticeFragment : Fragment() {

    lateinit var binding: FragmentUnreadNoticeBinding
    lateinit var mainActivity: MainActivity

    var numOfUnreadNotice = 0
    var isScrap = false
    var isLike = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentUnreadNoticeBinding.inflate(layoutInflater)
        mainActivity = activity as MainActivity

        initView()

        binding.run {
            buttonLikeNotice.setOnClickListener {
                isLike = !isLike
                if (isLike) {
                    buttonLikeNotice.setImageResource(R.drawable.ic_like_primary50)
                } else {
                    buttonLikeNotice.setImageResource(R.drawable.ic_like_primary20)
                }
            }

            buttonScrap.setOnClickListener {
                isScrap = !isScrap
                if (isScrap) {
                    buttonScrap.text = "저장 취소"
                    BasicToast.createToastAboveView(
                        requireContext(),
                        "저장한 공지사항에 추가했어요 ⭐",
                        binding.layoutUnreadNoticeButton
                    )
                } else {
                    buttonScrap.text = "저장하기"
                    BasicToast.createToastAboveView(
                        requireContext(),
                        "저장한 공지사항을 취소했어요 ⭐",
                        binding.layoutUnreadNoticeButton
                    )
                }
            }

            buttonNext.setOnClickListener {
                // 다음 공지사항으로 이동
            }
        }

        return binding.root
    }

    fun initView() {

        mainActivity.hideBottomNavigation(true)
        mainActivity.hideWriteNoticeButton(true)

        binding.run {

            toolbar.run {
                buttonBack.setOnClickListener {
                    fragmentManager?.popBackStack()
                }
                textViewTitle.text = "${numOfUnreadNotice}개 남음"
            }
            // Adapter 연결
//            adapter = AnnouncementPagerAdapter(requireContext(), announcements)
//            viewPager.adapter = adapter

            // DotsIndicator 연결
//            attachTo(viewPager)
        }
    }
}