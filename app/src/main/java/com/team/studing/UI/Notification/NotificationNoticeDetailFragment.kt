package com.team.studing.UI.Notification

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.team.studing.API.response.Home.NoticeDetailResponse
import com.team.studing.MainActivity
import com.team.studing.NotificationNoticeActivity
import com.team.studing.R
import com.team.studing.UI.Notice.Adapter.NoticeImagePagerAdapter
import com.team.studing.Utils.BasicToast
import com.team.studing.ViewModel.HomeViewModel
import com.team.studing.ViewModel.NoticeViewModel
import com.team.studing.databinding.FragmentNotificationNoticeDetailBinding


class NotificationNoticeDetailFragment : Fragment() {

    lateinit var binding: FragmentNotificationNoticeDetailBinding
    lateinit var notificationActivity: NotificationNoticeActivity

    private val viewModel: HomeViewModel by lazy {
        ViewModelProvider(notificationActivity)[HomeViewModel::class.java]
    }
    private val noticeViewModel: NoticeViewModel by lazy {
        ViewModelProvider(notificationActivity)[NoticeViewModel::class.java]
    }

    var isLike: Boolean? = false
    var isScrap: Boolean? = false
    var isView: Boolean? = false

    private var getNoticeDetail: NoticeDetailResponse? = null
    private lateinit var noticeImageAdapter: NoticeImagePagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentNotificationNoticeDetailBinding.inflate(layoutInflater)
        notificationActivity = activity as NotificationNoticeActivity

        observeViewModel()
        initView()

        binding.run {

            noticeImageAdapter = NoticeImagePagerAdapter(
                notificationActivity,
                getNoticeDetail?.images
            )

            buttonScrap.setOnClickListener {
                if (!isScrap!!) {
                    noticeViewModel.scrapNotice(notificationActivity, getNoticeDetail?.id!!)
                } else {
                    noticeViewModel.cancelScrapNotice(notificationActivity, getNoticeDetail?.id!!)
                }
            }

            buttonLikeNotice.setOnClickListener {

                if (!isLike!!) {
                    noticeViewModel.likeNotice(notificationActivity, getNoticeDetail?.id!!)
                } else {
                    noticeViewModel.cancelLikeNotice(notificationActivity, getNoticeDetail?.id!!)
                }
            }
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        getNoticeDetail = null
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModelStore.clear() // Fragment 전체 소멸 시 초기화
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModelStore.clear()
    }

    fun observeViewModel() {
        noticeViewModel.run {
            noticeDetail.observe(viewLifecycleOwner) {
                getNoticeDetail = it
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

    fun setData() {
        binding.run {
            layoutNoticeDetail.run {
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
                Glide.with(notificationActivity).load(getNoticeDetail?.logoImage)
                    .into(imageViewStudentCouncil)

                chipNoticeType.text = getNoticeDetail?.tag
                if (getNoticeDetail?.tag == "공지") {
                    chipNoticeType.run {
                        setBackgroundResource(R.drawable.background_notice_type_chip_primary20)
                        setTextColor(resources.getColor(R.color.primary_50))
                    }
                } else {
                    chipNoticeType.run {
                        setBackgroundResource(R.drawable.background_notice_type_chip_red)
                        setTextColor(resources.getColor(R.color.red))
                    }
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


    fun initView() {
        binding.run {

            toolbar.run {
                buttonBack.setOnClickListener {
                    val mainIntent = Intent(activity, MainActivity::class.java)
                    mainIntent.putExtra("isLogin", true)
                    activity?.startActivity(mainIntent)
                }
                textViewTitle.text = "공지사항"
            }
        }
    }
}