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
import com.team.studing.API.response.Home.ScrapNotice
import com.team.studing.MainActivity
import com.team.studing.R
import com.team.studing.UI.Home.Adapter.ScrapNoticeListAdapter
import com.team.studing.UI.Home.Adapter.StudentCouncilAdapter
import com.team.studing.Utils.MyApplication
import com.team.studing.ViewModel.HomeViewModel
import com.team.studing.ViewModel.NoticeViewModel
import com.team.studing.databinding.FragmentScrapNoticeListBinding

class ScrapNoticeListFragment : Fragment() {

    lateinit var binding: FragmentScrapNoticeListBinding
    lateinit var mainActivity: MainActivity
    lateinit var viewModel: HomeViewModel
    lateinit var noticeViewModel: NoticeViewModel

    private lateinit var studentCouncilAdapter: StudentCouncilAdapter
    private lateinit var scrapNoticeListAdapter: ScrapNoticeListAdapter

    private var getStudentCouncilLogoList = mutableListOf<String>()
    private var getStudentCouncilNameList = mutableListOf<String>()
    private var getUnReadStudentCouncilNameList = mutableListOf<String>()
    private var getScrapNoticeList = mutableListOf<ScrapNotice>()

    private var categoryPosition = 0
    private var isRegisterMajorStudentCouncil = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentScrapNoticeListBinding.inflate(layoutInflater)
        mainActivity = activity as MainActivity
        viewModel = ViewModelProvider(mainActivity)[HomeViewModel::class.java]
        noticeViewModel = ViewModelProvider(mainActivity)[NoticeViewModel::class.java]

        initAdapter()
        observeViewModel()

        binding.run {
            emptyViewStudentCouncil.buttonRegisterStudentCouncil.setOnClickListener {
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

    fun initAdapter() {
        studentCouncilAdapter = StudentCouncilAdapter(
            mainActivity,
            getStudentCouncilNameList,
            getStudentCouncilLogoList,
            getUnReadStudentCouncilNameList
        ).apply {
            itemClickListener = object : StudentCouncilAdapter.OnItemClickListener {
                override fun onItemClick(position: Int) {
                    categoryPosition = position
                    viewModel.getScrapNoticeList(
                        mainActivity,
                        MyApplication.categoryList[categoryPosition]
                    )
                    updateNoticeListVisibility()
                }
            }
        }

        scrapNoticeListAdapter = ScrapNoticeListAdapter(
            mainActivity,
            getScrapNoticeList
        ).apply {
            itemClickListener = object : ScrapNoticeListAdapter.OnItemClickListener {
                override fun onItemClick(position: Int) {
                    MyApplication.noticeId = getScrapNoticeList[position].id.toInt()
                    noticeViewModel.getNoticeDetail(
                        mainActivity,
                        getScrapNoticeList[position].id.toInt()
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

        binding.recyclerViewScrapNoticeList.apply {
            adapter = scrapNoticeListAdapter
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

//            unreadStudentCouncilNameList.observe(viewLifecycleOwner) {
//                getUnReadStudentCouncilNameList = it
//                studentCouncilAdapter.updateList(
//                    getStudentCouncilNameList,
//                    getStudentCouncilLogoList,
//                    getUnReadStudentCouncilNameList
//                )
//            }

            scrapNoticeList.observe(viewLifecycleOwner) {
                getScrapNoticeList = it
                scrapNoticeListAdapter.updateList(getScrapNoticeList)
                updateNoticeListVisibility()
            }
        }
    }

    private fun updateNoticeListVisibility() {
        Log.d("##", "${isRegisterMajorStudentCouncil}, ${categoryPosition}")
        binding.run {
            if (!isRegisterMajorStudentCouncil && categoryPosition == 3) {
                Log.d("##", "no studentCouncil")
                recyclerViewScrapNoticeList.visibility = View.GONE
                emptyViewScrapNotice.layoutEmptyNoticeList.visibility = View.GONE
                emptyViewStudentCouncil.layoutEmptyStudentCouncil.visibility = View.VISIBLE
            } else {
                if (getScrapNoticeList.isEmpty()) {
                    Log.d("##", "empty : ${getScrapNoticeList}")
                    recyclerViewScrapNoticeList.visibility = View.GONE
                    emptyViewScrapNotice.layoutEmptyNoticeList.visibility = View.VISIBLE
                    emptyViewStudentCouncil.layoutEmptyStudentCouncil.visibility = View.GONE
                } else {
                    Log.d("##", "not empty : ${getScrapNoticeList}")
                    recyclerViewScrapNoticeList.visibility = View.VISIBLE
                    emptyViewScrapNotice.layoutEmptyNoticeList.visibility = View.GONE
                    emptyViewStudentCouncil.layoutEmptyStudentCouncil.visibility = View.GONE
                }
            }
        }
    }

    fun initView() {
        mainActivity.hideBottomNavigation(true)
        mainActivity.hideWriteNoticeButton(true)

        viewModel.getScrapNoticeList(mainActivity, MyApplication.categoryList[categoryPosition])

        binding.run {
            toolbar.run {
                textViewTitle.text = "저장한 공지사항을 확인해요"
                buttonBack.setOnClickListener {
                    fragmentManager?.popBackStack()
                }
            }
        }
    }
}