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
import com.team.studing.MainActivity
import com.team.studing.R
import com.team.studing.UI.Home.Adapter.StudentCouncilAdapter
import com.team.studing.Utils.MyApplication
import com.team.studing.ViewModel.HomeViewModel
import com.team.studing.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding
    lateinit var mainActivity: MainActivity
    lateinit var viewModel: HomeViewModel


    var getStudentCouncilLogoList = mutableListOf<String>()
    var getStudentCouncilNameList = mutableListOf<String>()
    var getUnReadStudentCouncilNameList = mutableListOf<String>()

    lateinit var studentCouncilAdapter: StudentCouncilAdapter
//    private lateinit var adapter: AnnouncementPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentHomeBinding.inflate(layoutInflater)
        mainActivity = activity as MainActivity
        viewModel = ViewModelProvider(mainActivity)[HomeViewModel::class.java]

        viewModel.run {
            studentCouncilNameList.observe(mainActivity) {
                getStudentCouncilNameList = it
            }
            studentCouncilLogoList.observe(mainActivity) {
                getStudentCouncilLogoList = it
                Log.d("##", "${getStudentCouncilNameList}")
            }
            unreadStudentCouncilNameList.observe(mainActivity) {
                getUnReadStudentCouncilNameList = it
                // Adapter 초기화 및 RecyclerView 설정
                studentCouncilAdapter = StudentCouncilAdapter(
                    mainActivity,
                    getStudentCouncilNameList,
                    getStudentCouncilLogoList,
                    getUnReadStudentCouncilNameList
                )
                binding.recyclerViewStudentCouncil.adapter = studentCouncilAdapter
                binding.recyclerViewStudentCouncil.layoutManager =
                    LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            }
        }

        initView()

        binding.run {
            buttonShowUnread.setOnClickListener {
                // 놓친 공지사항 확인하기 화면으로 전환
                val nextFragment = UnreadNoticeFragment()

                val transaction = mainActivity.supportFragmentManager.beginTransaction()
                transaction.replace(R.id.fragmentContainerView_main, nextFragment)
                transaction.addToBackStack(null)
                transaction.commit()
                true
            }

            buttonNoticeMore.setOnClickListener {
                // 공지사항 리스트 화면으로 전환
                val nextFragment = NoticeListFragment()

                val transaction = mainActivity.supportFragmentManager.beginTransaction()
                transaction.replace(R.id.fragmentContainerView_main, nextFragment)
                transaction.addToBackStack(null)
                transaction.commit()
                true
            }

            buttonNoticeScrapMore.setOnClickListener {
                // 저장한 공지사항 리스트 화면으로 전환
                val nextFragment = ScrapNoticeListFragment()

                val transaction = mainActivity.supportFragmentManager.beginTransaction()
                transaction.replace(R.id.fragmentContainerView_main, nextFragment)
                transaction.addToBackStack(null)
                transaction.commit()
                true
            }

            layoutEmptyNotice.buttonRegisterStudentCouncil.setOnClickListener {
                // 학생회 등록 구글폼
                var intent =
                    Intent(Intent.ACTION_VIEW, Uri.parse("https://forms.gle/cCyRaN41xGuqQffM6"))
                startActivity(intent)
            }

            layoutEmptyScrapNotice.buttonShowWholeNotice.setOnClickListener {
                // 공지사항 리스트 화면으로 전환
                val nextFragment = NoticeListFragment()

                val transaction = mainActivity.supportFragmentManager.beginTransaction()
                transaction.replace(R.id.fragmentContainerView_main, nextFragment)
                transaction.addToBackStack(null)
                transaction.commit()
                true
            }

        }

        return binding.root
    }

    fun initView() {
        binding.run {

            layoutEmptyScrapNotice.layoutEmptyHomeScrapNotice.visibility = View.GONE
            layoutEmptyNotice.layoutEmptyHomeNotice.visibility = View.GONE

            viewModel.getStudentCouncilLogo(mainActivity)
            viewModel.getUnreadStudentCouncil(mainActivity)

            textViewUnreadNoticeWithNickname.text = "${MyApplication.memberData?.name}님이 놓친 공지사항"

            // Adapter 연결
//            adapter = AnnouncementPagerAdapter(requireContext(), announcements)
//            viewPager.adapter = adapter

            // DotsIndicator 연결
//            attachTo(viewPager)
        }
    }
}