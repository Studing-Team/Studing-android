package com.team.studing.Home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.team.studing.MainActivity
import com.team.studing.R
import com.team.studing.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding
    lateinit var mainActivity: MainActivity

//    private lateinit var adapter: AnnouncementPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentHomeBinding.inflate(layoutInflater)
        mainActivity = activity as MainActivity

        initView()

        binding.run {

            buttonShowUnread.setOnClickListener {
                // 놓친 공지사항 확인하기 화면으로 전환
                val nextFragment = UnreadNoticeFragment()

                val transaction = mainActivity.supportFragmentManager.beginTransaction()
                transaction.replace(R.id.fragmentContainerView_main, nextFragment)
                transaction.commit()
                true
            }

            buttonNoticeMore.setOnClickListener {
                // 공지사항 리스트 화면으로 전환
                val nextFragment = NoticeListFragment()

                val transaction = mainActivity.supportFragmentManager.beginTransaction()
                transaction.replace(R.id.fragmentContainerView_main, nextFragment)
                transaction.commit()
                true
            }

            buttonNoticeScrapMore.setOnClickListener {
                // 저장한 공지사항 리스트 화면으로 전환
                val nextFragment = ScrapNoticeListFragment()

                val transaction = mainActivity.supportFragmentManager.beginTransaction()
                transaction.replace(R.id.fragmentContainerView_main, nextFragment)
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

            // Adapter 연결
//            adapter = AnnouncementPagerAdapter(requireContext(), announcements)
//            viewPager.adapter = adapter

            // DotsIndicator 연결
//            attachTo(viewPager)
        }
    }
}