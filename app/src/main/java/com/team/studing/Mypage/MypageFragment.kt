package com.team.studing.Mypage

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.team.studing.MainActivity
import com.team.studing.databinding.FragmentMypageBinding

class MypageFragment : Fragment() {

    lateinit var binding: FragmentMypageBinding
    lateinit var mainActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentMypageBinding.inflate(layoutInflater)
        mainActivity = activity as MainActivity

        binding.run {
            buttonLogout.setOnClickListener {
                // 로그아웃 dialog
            }

            buttonWithdrawal.setOnClickListener {

            }

            layoutQna.setOnClickListener {
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
    }
}