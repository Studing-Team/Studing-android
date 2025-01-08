package com.team.studing.UI.Notice

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.team.studing.API.response.Home.NoticeDetailResponse
import com.team.studing.MainActivity
import com.team.studing.R
import com.team.studing.UI.Notice.Adapter.NoticeImagePagerAdapter
import com.team.studing.Utils.BasicToast
import com.team.studing.Utils.GlobalApplication.Companion.amplitude
import com.team.studing.ViewModel.NoticeViewModel
import com.team.studing.databinding.FragmentNoticeDetailBinding

class NoticeDetailFragment : Fragment() {

    lateinit var binding: FragmentNoticeDetailBinding
    lateinit var mainActivity: MainActivity

    private val viewModel: NoticeViewModel by lazy {
        ViewModelProvider(mainActivity)[NoticeViewModel::class.java]
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

        binding = FragmentNoticeDetailBinding.inflate(layoutInflater)
        mainActivity = activity as MainActivity

        observeViewModel()
        initView()

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
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        initView()
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
        viewModel.run {
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
                Glide.with(mainActivity).load(getNoticeDetail?.logoImage)
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

        mainActivity.hideBottomNavigation(true)
        mainActivity.hideWriteNoticeButton(true)

        binding.run {

            toolbar.run {
                buttonBack.setOnClickListener {
                    fragmentManager?.popBackStack()
                }
                textViewTitle.text = "공지사항"

                buttonKebabMenu.visibility = View.VISIBLE
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
                            putInt("id", getNoticeDetail?.id!!)
                            putString("title", "${getNoticeDetail?.title}")
                            putString("content", "${getNoticeDetail?.content}")
                            putString("tag", "${getNoticeDetail?.tag}")
                            putStringArrayList("image", ArrayList(getNoticeDetail?.images))
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