package com.team.studing.UI.Home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.team.studing.API.response.Home.Notice
import com.team.studing.MainActivity
import com.team.studing.R
import com.team.studing.UI.Home.Adapter.NoticeListAdapter
import com.team.studing.UI.Home.Adapter.StudentCouncilAdapter
import com.team.studing.Utils.GlobalApplication.Companion.amplitude
import com.team.studing.Utils.MyApplication
import com.team.studing.ViewModel.HomeViewModel
import com.team.studing.ViewModel.NoticeViewModel
import com.team.studing.databinding.FragmentNoticeListBinding

class NoticeListFragment : Fragment() {

    lateinit var binding: FragmentNoticeListBinding
    lateinit var mainActivity: MainActivity
    lateinit var viewModel: HomeViewModel
    lateinit var noticeViewModel: NoticeViewModel

    private lateinit var studentCouncilAdapter: StudentCouncilAdapter
    private lateinit var noticeListAdapter: NoticeListAdapter

    private var getStudentCouncilLogoList = mutableListOf<String>()
    private var getStudentCouncilNameList = mutableListOf<String>()
    private var getUnReadStudentCouncilNameList = mutableListOf<String>()
    private var getNoticeList = mutableListOf<Notice>()

    private var categoryPosition = 0
    private var isRegisterMajorStudentCouncil = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentNoticeListBinding.inflate(layoutInflater)
        mainActivity = activity as MainActivity
        viewModel = ViewModelProvider(mainActivity)[HomeViewModel::class.java]
        noticeViewModel = ViewModelProvider(mainActivity)[NoticeViewModel::class.java]


        binding.run {
            buttonQna.setOnClickListener {
                amplitude.track("click_contact_kakao_list")

                // 스튜딩 카카오톡 채널
                var intent = Intent(Intent.ACTION_VIEW, Uri.parse("http://pf.kakao.com/_BzmZn"))
                startActivity(intent)
            }

            emptyViewStudentCouncil.buttonRegisterStudentCouncil.setOnClickListener {
                amplitude.track("click_register_form_list")

                val intent =
                    Intent(Intent.ACTION_VIEW, Uri.parse("https://forms.gle/cCyRaN41xGuqQffM6"))
                startActivity(intent)
            }
        }


        return binding.root
    }

    override fun onResume() {
        super.onResume()
        initView()
    }

    private fun setAmplitudeData(position: Int) {
        when (position) {
            0 -> {
                amplitude.track("click_category_all_list")
            }

            1 -> {
                amplitude.track("click_category_university_list")
            }

            2 -> {
                amplitude.track("click_category_college_list")
            }

            3 -> {
                amplitude.track("click_category_department_list")
            }
        }
    }

    fun initAdapter() {
        studentCouncilAdapter = StudentCouncilAdapter(
            mainActivity,
            getStudentCouncilNameList,
            getStudentCouncilLogoList,
            getUnReadStudentCouncilNameList
        ).apply {
            updateSelectedPosition(categoryPosition)
            itemClickListener = object : StudentCouncilAdapter.OnItemClickListener {
                override fun onItemClick(position: Int) {
                    setAmplitudeData(position)

                    categoryPosition = position
                    MyApplication.noticeCategory = categoryPosition
                    if (categoryPosition == 0) {
                        viewModel.getNoticeList(mainActivity)
                    } else {
                        viewModel.getStudentCouncilNoticeList(
                            mainActivity,
                            MyApplication.categoryList[categoryPosition]
                        )
                    }
                    updateNoticeListVisibility()
                }
            }
        }

        noticeListAdapter = NoticeListAdapter(
            mainActivity,
            getNoticeList
        ).apply {
            itemClickListener = object : NoticeListAdapter.OnItemClickListener {
                override fun onItemClick(position: Int) {
                    amplitude.track("click_detail_notice_list")

                    MyApplication.noticeId = getNoticeList[position].id
                    noticeViewModel.getNoticeDetail(
                        mainActivity,
                        MyApplication.noticeId
                    )
                    mainActivity.supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerView_main, NoticeDetailFragment())
                        .addToBackStack(null)
                        .commit()
                }
            }
        }

        binding.recyclerViewStudentCouncil.apply {
            adapter = studentCouncilAdapter
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        }

        binding.recyclerViewNoticeList.apply {
            adapter = noticeListAdapter
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        }
    }

    private fun observeViewModel() {
        viewModel.run {
            majorStudentCouncil.observe(viewLifecycleOwner) {
                isRegisterMajorStudentCouncil = it
                updateNoticeListVisibility()
            }

            studentCouncilNameList.observe(viewLifecycleOwner) {
                getStudentCouncilNameList = it
                studentCouncilAdapter.updateList(
                    getStudentCouncilNameList,
                    getStudentCouncilLogoList,
                    getUnReadStudentCouncilNameList
                )
            }

            studentCouncilLogoList.observe(viewLifecycleOwner) {
                getStudentCouncilLogoList = it
                studentCouncilAdapter.updateList(
                    getStudentCouncilNameList,
                    getStudentCouncilLogoList,
                    getUnReadStudentCouncilNameList
                )
            }

            unreadStudentCouncilNameList.observe(viewLifecycleOwner) {
                getUnReadStudentCouncilNameList = it
                studentCouncilAdapter.updateList(
                    getStudentCouncilNameList,
                    getStudentCouncilLogoList,
                    getUnReadStudentCouncilNameList
                )
            }

            noticeList.observe(viewLifecycleOwner) {
                getNoticeList = it
                noticeListAdapter.updateList(getNoticeList)
                updateNoticeListVisibility()
            }

            studentCouncilNoticeList.observe(viewLifecycleOwner) {
                getNoticeList = it
                noticeListAdapter.updateList(getNoticeList)
                updateNoticeListVisibility()
            }
        }
    }

    private fun updateNoticeListVisibility() {
        Log.d("##", "${isRegisterMajorStudentCouncil}, ${categoryPosition}")
        binding.run {
            if (!isRegisterMajorStudentCouncil && categoryPosition == 3) {
                Log.d("##", "no studentCouncil")
                recyclerViewNoticeList.visibility = View.GONE
                emptyViewNotice.layoutEmptyNoticeList.visibility = View.GONE
                emptyViewStudentCouncil.layoutEmptyStudentCouncil.visibility = View.VISIBLE
            } else {
                if (getNoticeList.isEmpty()) {
                    Log.d("##", "empty : ${getNoticeList}")
                    recyclerViewNoticeList.visibility = View.GONE
                    emptyViewNotice.layoutEmptyNoticeList.visibility = View.VISIBLE
                    emptyViewStudentCouncil.layoutEmptyStudentCouncil.visibility = View.GONE
                } else {
                    Log.d("##", "not empty : ${getNoticeList}")
                    recyclerViewNoticeList.visibility = View.VISIBLE
                    emptyViewNotice.layoutEmptyNoticeList.visibility = View.GONE
                    emptyViewStudentCouncil.layoutEmptyStudentCouncil.visibility = View.GONE
                }
            }
        }
    }


    fun initView() {
        mainActivity.hideBottomNavigation(true)
        mainActivity.hideWriteNoticeButton(true)

        categoryPosition = MyApplication.noticeCategory
        // 카테고리 초기 선택 및 스크롤
        binding.recyclerViewStudentCouncil.post {
            studentCouncilAdapter.updateSelectedPosition(categoryPosition) // 어댑터에 선택 상태 전달
            binding.recyclerViewStudentCouncil.smoothScrollToPosition(categoryPosition)
        }
        if (categoryPosition == 0) {
            viewModel.getNoticeList(mainActivity)
        } else {
            viewModel.getStudentCouncilNoticeList(
                mainActivity,
                MyApplication.categoryList[categoryPosition]
            )
        }

        initAdapter()
        observeViewModel()

        binding.run {
            recyclerViewNoticeList.visibility = View.VISIBLE
            emptyViewNotice.layoutEmptyNoticeList.visibility = View.GONE
            emptyViewStudentCouncil.layoutEmptyStudentCouncil.visibility = View.GONE

            toolbar.run {
                textViewTitle.text = "학생회 공지 리스트"
                buttonBack.setOnClickListener {
                    fragmentManager?.popBackStack()
                }
            }
        }
    }
}