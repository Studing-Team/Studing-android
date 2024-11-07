package com.team.studing.UI.Mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.team.studing.MainActivity
import com.team.studing.databinding.FragmentMypageWithdrawalBinding

class MypageWithdrawalFragment : Fragment() {

    lateinit var binding: FragmentMypageWithdrawalBinding
    lateinit var mainActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentMypageWithdrawalBinding.inflate(layoutInflater)

        initView()

        binding.run {
            buttonCancel.setOnClickListener {
                fragmentManager?.popBackStack()
            }

            buttonWithdrawal.setOnClickListener {
                // 회원 탈퇴 dialog
            }
        }

        return binding.root
    }

    fun initView() {
        mainActivity.hideBottomNavigation(true)
        mainActivity.hideWriteNoticeButton(true)

        binding.toolbar.run {
            buttonBack.setOnClickListener {
                fragmentManager?.popBackStack()
            }
        }
    }
}