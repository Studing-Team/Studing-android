package com.team.studing.UI.Home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.team.studing.API.response.Home.NoticeDetailResponse
import com.team.studing.MainActivity
import com.team.studing.R
import com.team.studing.UI.Home.Adapter.NoticeImagePagerAdapter
import com.team.studing.Utils.BasicToast
import com.team.studing.ViewModel.HomeViewModel
import com.team.studing.databinding.FragmentNoticeDetailBinding

class NoticeDetailFragment : Fragment() {

    lateinit var binding: FragmentNoticeDetailBinding
    lateinit var mainActivity: MainActivity
    lateinit var viewModel: HomeViewModel

    var isLike = false
    var isScrap = false

    private var getNoticeDetail: NoticeDetailResponse? = null
    private lateinit var noticeImageAdapter: NoticeImagePagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentNoticeDetailBinding.inflate(layoutInflater)
        mainActivity = activity as MainActivity
        viewModel = ViewModelProvider(mainActivity)[HomeViewModel::class.java]

        observeViewModel()
        initView()

        binding.run {

            noticeImageAdapter = NoticeImagePagerAdapter(
                mainActivity,
                getNoticeDetail?.images
            )

            buttonScrap.setOnClickListener {
                isScrap = !isScrap
                if (isScrap) {
                    BasicToast.createToastAboveView(
                        requireContext(),
                        "저장한 공지사항에 추가했어요 ⭐",
                        binding.buttonScrap
                    )
                    layoutNoticeDetail.imageViewScrap.setImageResource(R.drawable.ic_scrap_selected)
                    buttonScrap.text = "저장 취소"
                    layoutNoticeDetail.textViewScrapNum.text = "${getNoticeDetail?.saveCount!! + 1}"
                } else {
                    BasicToast.createToastAboveView(
                        requireContext(),
                        "저장한 공지사항을 취소했어요 ⭐",
                        binding.buttonScrap
                    )
                    layoutNoticeDetail.imageViewScrap.setImageResource(R.drawable.ic_scrap_black30)
                    buttonScrap.text = "저장하기"
                    layoutNoticeDetail.textViewScrapNum.text = "${getNoticeDetail?.saveCount}"
                }
            }

            buttonLikeNotice.setOnClickListener {
                isLike = !isLike
                if (isLike) {
                    buttonLikeNotice.setImageResource(R.drawable.ic_like_primary50)
                    layoutNoticeDetail.imageViewLike.setImageResource(R.drawable.ic_like_red)
                    layoutNoticeDetail.textViewLikeNum.text = "${getNoticeDetail?.likeCount!! + 1}"
                } else {
                    buttonLikeNotice.setImageResource(R.drawable.ic_like_primary20)
                    layoutNoticeDetail.imageViewLike.setImageResource(R.drawable.ic_like_black30)
                    layoutNoticeDetail.textViewLikeNum.text = "${getNoticeDetail?.likeCount}"
                }
            }
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        getNoticeDetail = null
    }

    fun observeViewModel() {
        viewModel.run {
            noticeDetail.observe(viewLifecycleOwner) {
                getNoticeDetail = it

                setData()
            }
        }
    }

    fun setData() {
        binding.run {
            layoutNoticeDetail.run {
                textViewNoticeTitle.text = getNoticeDetail?.title
                textViewNoticeContent.text = getNoticeDetail?.content
                textViewLikeNum.text = getNoticeDetail?.likeCount.toString()
                if (getNoticeDetail?.likeCheck == true) {
                    imageViewLike.setImageResource(R.drawable.ic_like_red)
                } else {
                    imageViewLike.setImageResource(R.drawable.ic_like_black30)
                }
                textViewScrapNum.text = getNoticeDetail?.saveCount.toString()
                if (getNoticeDetail?.saveCheck == true) {
                    imageViewScrap.setImageResource(R.drawable.ic_scrap_selected)
                } else {
                    imageViewScrap.setImageResource(R.drawable.ic_scrap_black30)
                }
                textViewShowNum.text = getNoticeDetail?.readCount.toString()
                textViewDate.text = getNoticeDetail?.createdAt
                textViewStudentCouncil.text = getNoticeDetail?.affilitionName
                Glide.with(mainActivity).load(getNoticeDetail?.logoImage)
                    .into(imageViewStudentCouncil)

                chipNoticeType.text = getNoticeDetail?.tag
                if (getNoticeDetail?.tag == "공지") {
                    chipNoticeType.run {
                        setBackgroundResource(R.drawable.background_notice_type_chip_red)
                        setTextColor(resources.getColor(R.color.red))
                    }
                } else {
                    chipNoticeType.run {
                        setBackgroundResource(R.drawable.background_notice_type_chip_primary20)
                        setTextColor(resources.getColor(R.color.primary_50))
                    }
                }

                if (getNoticeDetail?.images.isNullOrEmpty()) {
                    viewPager.visibility = View.GONE
                    dotsIndicatorNotice.visibility = View.GONE
                } else {
                    viewPager.visibility = View.VISIBLE
                    dotsIndicatorNotice.visibility = View.VISIBLE
                    noticeImageAdapter.updateList(getNoticeDetail?.images)
                    viewPager.apply {
                        adapter = noticeImageAdapter
                        dotsIndicatorNotice.attachTo(this)
                    }
                }
            }
        }
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