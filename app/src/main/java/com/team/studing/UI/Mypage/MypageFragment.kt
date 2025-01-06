package com.team.studing.UI.Mypage

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.team.studing.API.TokenManager
import com.team.studing.API.response.Mypage.MyPageInfoResponse
import com.team.studing.MainActivity
import com.team.studing.R
import com.team.studing.Utils.GlobalApplication.Companion.amplitude
import com.team.studing.ViewModel.MypageViewModel
import com.team.studing.databinding.FragmentMypageBinding

class MypageFragment : Fragment() {

    lateinit var binding: FragmentMypageBinding
    lateinit var mainActivity: MainActivity
    lateinit var viewModel: MypageViewModel

    var userInfo: MyPageInfoResponse? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentMypageBinding.inflate(layoutInflater)
        mainActivity = activity as MainActivity
        viewModel = ViewModelProvider(this)[MypageViewModel::class.java]

        viewModel.run {
            user.value?.let { member ->
                Log.d("##", "userInfo : ${member}")
                binding.run {
                    textViewName.text = member.name
                    textViewUniversity.text = member.memberUniversity
                    textViewMajor.text = member.memberDepartment
                    textViewStudentnum.text = "${member.admissionNumber}학번"
                }
            }
            user.observe(mainActivity) {
                userInfo = it
                Log.d("##", "userInfo change : ${userInfo}")

                binding.run {
                    textViewName.text = userInfo?.name
                    textViewUniversity.text = userInfo?.memberUniversity
                    textViewMajor.text = userInfo?.memberDepartment
                    textViewStudentnum.text = "${userInfo?.admissionNumber}학번"
                }
            }
        }

        initView()

        binding.run {
            buttonLogout.setOnClickListener {
                amplitude.track("click_logout_mypage")

                var tokenManager = TokenManager(mainActivity)
                // 로그아웃 dialog
                val dialog = DialogLogout()

                dialog.setLogoutDialogInterface(object : LogoutDialogInterface {
                    override fun onClickYesButton() {
                        amplitude.track("click_logout_complete_mypage")

                        // 로그아웃 기능 구현
                        tokenManager.deleteAccessToken()
                        amplitude.reset()

                        mainActivity.finish()
                    }
                })

                dialog.show(parentFragmentManager, "DialogWithdrawal")

            }

            buttonWithdrawal.setOnClickListener {
                amplitude.track("click_signout_mypage")

                mainActivity.supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainerView_main, MypageWithdrawalFragment())
                    .addToBackStack(null)
                    .commit()
            }

            layoutQna.setOnClickListener {
                amplitude.track("click_contact_mypage")

                // 스튜딩 카카오톡 채널
                var intent = Intent(Intent.ACTION_VIEW, Uri.parse("http://pf.kakao.com/_BzmZn"))
                startActivity(intent)
            }

            layoutAgreement1.setOnClickListener {
                // 서비스 이용약관 노션 페이지
                var intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://studingofficial.notion.site/11905c1258e080ee91cecfb7ff633bab")
                )
                startActivity(intent)
            }

            layoutAgreement2.setOnClickListener {
                // 개인정보 수집 및 이용동의 노션 페이지
                var intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://studingofficial.notion.site/11905c1258e08063bba2f82d320de454")
                )
                startActivity(intent)
            }
        }

        return binding.root
    }

    fun initView() {
        mainActivity.hideWriteNoticeButton(true)
        viewModel.getUserInfo(mainActivity)
    }
}