package com.team.studing.UI.Mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.team.studing.MainActivity
import com.team.studing.ViewModel.MypageViewModel
import com.team.studing.databinding.FragmentMypageWithdrawalBinding

class MypageWithdrawalFragment : Fragment() {

    lateinit var binding: FragmentMypageWithdrawalBinding
    lateinit var mainActivity: MainActivity
    lateinit var viewModel: MypageViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentMypageWithdrawalBinding.inflate(layoutInflater)
        mainActivity = activity as MainActivity
        viewModel = ViewModelProvider(this)[MypageViewModel::class.java]

        initView()

        binding.run {
            buttonCancel.setOnClickListener {
                fragmentManager?.popBackStack()
            }

            buttonWithdrawal.setOnClickListener {
                // 회원 탈퇴 dialog
                val dialog = DialogWithdrawal()

                dialog.setSignUpDialogInterface(object : WithdrawalDialogInterface {
                    override fun onClickYesButton() {
                        // 탈퇴하기 기능 구현
                        viewModel.withdrawal(mainActivity)
                    }
                })

                dialog.show(parentFragmentManager, "DialogWithdrawal")
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