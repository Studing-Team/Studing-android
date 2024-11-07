package com.team.studing.UI.Home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.team.studing.MainActivity
import com.team.studing.R
import com.team.studing.Utils.BasicToast
import com.team.studing.databinding.FragmentNoticeDetailBinding

class NoticeDetailFragment : Fragment() {

    lateinit var binding: FragmentNoticeDetailBinding
    lateinit var mainActivity: MainActivity

    var isLike = false
    var isScrap = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentNoticeDetailBinding.inflate(layoutInflater)
        mainActivity = activity as MainActivity

        initView()

        binding.run {
            buttonScrap.setOnClickListener {
                isScrap = !isScrap
                if (isScrap) {
                    BasicToast.createToastAboveView(
                        requireContext(),
                        "저장한 공지사항에 추가했어요 ⭐",
                        binding.buttonScrap
                    )
                } else {
                    BasicToast.createToastAboveView(
                        requireContext(),
                        "저장한 공지사항을 취소했어요 ⭐",
                        binding.buttonScrap
                    )
                }
            }

            buttonLikeNotice.setOnClickListener {
                isLike = !isLike
                if (isLike) {
                    buttonLikeNotice.setImageResource(R.drawable.ic_like_primary50)
                } else {
                    buttonLikeNotice.setImageResource(R.drawable.ic_like_primary20)
                }
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
                textViewTitle.text = "공지사항"
            }
        }
    }
}