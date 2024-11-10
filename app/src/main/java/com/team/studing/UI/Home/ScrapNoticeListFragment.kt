package com.team.studing.UI.Home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.team.studing.MainActivity
import com.team.studing.databinding.FragmentScrapNoticeListBinding

class ScrapNoticeListFragment : Fragment() {

    lateinit var binding: FragmentScrapNoticeListBinding
    lateinit var mainActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentScrapNoticeListBinding.inflate(layoutInflater)
        mainActivity = activity as MainActivity

        initView()

        binding.run {

        }

        return binding.root
    }

    fun initView() {
        mainActivity.hideBottomNavigation(true)
        mainActivity.hideWriteNoticeButton(true)

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