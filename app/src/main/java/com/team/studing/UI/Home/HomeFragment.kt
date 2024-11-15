package com.team.studing.UI.Home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
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
import com.team.studing.UI.Home.Adapter.HomeNoticePagerAdapter
import com.team.studing.UI.Home.Adapter.StudentCouncilAdapter
import com.team.studing.Utils.MyApplication
import com.team.studing.ViewModel.HomeViewModel
import com.team.studing.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var mainActivity: MainActivity
    private lateinit var viewModel: HomeViewModel

    private var getStudentCouncilLogoList = mutableListOf<String>()
    private var getStudentCouncilNameList = mutableListOf<String>()
    private var getUnReadStudentCouncilNameList = mutableListOf<String>()
    private var getRecentNoticeList = mutableListOf<Notice>()

    private var categoryPosition = 0

    private lateinit var studentCouncilAdapter: StudentCouncilAdapter
    private lateinit var homeNoticePagerAdapter: HomeNoticePagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        mainActivity = activity as MainActivity
        viewModel = ViewModelProvider(mainActivity)[HomeViewModel::class.java]

        initAdapters()
        observeViewModel()

        binding.run {
            buttonShowUnread.setOnClickListener { navigateToUnreadNoticeFragment() }
            buttonNoticeMore.setOnClickListener { navigateToNoticeListFragment() }
            buttonNoticeScrapMore.setOnClickListener { navigateToScrapNoticeListFragment() }
            layoutEmptyNotice.buttonRegisterStudentCouncil.setOnClickListener { openGoogleForm() }
            layoutEmptyScrapNotice.buttonShowWholeNotice.setOnClickListener { navigateToNoticeListFragment() }
        }

        initView()

        return binding.root
    }

    private fun initAdapters() {
        // 어댑터 초기화
        studentCouncilAdapter = StudentCouncilAdapter(
            mainActivity,
            getStudentCouncilNameList,
            getStudentCouncilLogoList,
            getUnReadStudentCouncilNameList
        ).apply {
            itemClickListener = object : StudentCouncilAdapter.OnItemClickListener {
                override fun onItemClick(position: Int) {
                    categoryPosition = position
                    viewModel.getRecentNotice(
                        mainActivity,
                        MyApplication.categoryList[categoryPosition]
                    )
                }
            }
        }

        homeNoticePagerAdapter = HomeNoticePagerAdapter(
            mainActivity,
            getRecentNoticeList
        ).apply {
            itemClickListener = object : HomeNoticePagerAdapter.OnItemClickListener {
                override fun onItemClick(position: Int) {
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

        binding.viewPager.apply {
            adapter = homeNoticePagerAdapter
            binding.dotsIndicatorNotice.attachTo(this)
        }
    }

    private fun observeViewModel() {
        viewModel.run {
            studentCouncilNameList.observe(viewLifecycleOwner) {
                getStudentCouncilNameList = it
                studentCouncilAdapter.updateList(
                    getStudentCouncilNameList,
                    getStudentCouncilLogoList
                )
            }

            studentCouncilLogoList.observe(viewLifecycleOwner) {
                getStudentCouncilLogoList = it
                studentCouncilAdapter.updateList(
                    getStudentCouncilNameList,
                    getStudentCouncilLogoList
                )
            }

            unreadStudentCouncilNameList.observe(viewLifecycleOwner) {
                getUnReadStudentCouncilNameList = it
            }

            recentNoticeList.observe(viewLifecycleOwner) {
                getRecentNoticeList = it
                homeNoticePagerAdapter.updateList(getRecentNoticeList)

                binding.run {
                    if (getRecentNoticeList.isEmpty()) {
                        viewPager.visibility = View.GONE
                        dotsIndicatorNotice.visibility = View.GONE
                        layoutEmptyNotice.layoutEmptyHomeNotice.visibility = View.VISIBLE
                    } else {
                        viewPager.visibility = View.VISIBLE
                        dotsIndicatorNotice.visibility = View.VISIBLE
                        layoutEmptyNotice.layoutEmptyHomeNotice.visibility = View.GONE
                        layoutEmptyStudentCouncil.layoutEmptyStudentCouncil.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun initView() {
        binding.run {
            layoutEmptyScrapNotice.layoutEmptyHomeScrapNotice.visibility = View.GONE
            layoutEmptyNotice.layoutEmptyHomeNotice.visibility = View.GONE

            // 리스트 데이터 초기화
            viewModel.getStudentCouncilLogo(mainActivity)
            viewModel.getUnreadStudentCouncil(mainActivity)
            viewModel.getRecentNotice(mainActivity, MyApplication.categoryList[categoryPosition])

            textViewUnreadNoticeWithNickname.text = "${MyApplication.memberData?.name}님이 놓친 공지사항"
        }
    }

    private fun navigateToUnreadNoticeFragment() {
        mainActivity.supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView_main, UnreadNoticeFragment())
            .addToBackStack(null)
            .commit()
    }

    private fun navigateToNoticeListFragment() {
        mainActivity.supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView_main, NoticeListFragment())
            .addToBackStack(null)
            .commit()
    }

    private fun navigateToScrapNoticeListFragment() {
        mainActivity.supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView_main, ScrapNoticeListFragment())
            .addToBackStack(null)
            .commit()
    }

    private fun openGoogleForm() {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://forms.gle/cCyRaN41xGuqQffM6"))
        startActivity(intent)
    }
}
