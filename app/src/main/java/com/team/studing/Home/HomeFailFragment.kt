package com.team.studing.Home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.team.studing.LoginActivity
import com.team.studing.MainActivity
import com.team.studing.R
import com.team.studing.SignUp.SignUpStep2Fragment
import com.team.studing.SignUp.SignUpStep6Fragment
import com.team.studing.Utils.MainUtil.setStatusBarTransparent
import com.team.studing.databinding.FragmentHomeFailBinding

class HomeFailFragment : Fragment() {

    lateinit var binding: FragmentHomeFailBinding
    lateinit var mainActivity: MainActivity
    lateinit var loginActivity: LoginActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHomeFailBinding.inflate(layoutInflater)
        mainActivity = activity as MainActivity
        loginActivity = activity as LoginActivity

        initView()

        binding.run {

            buttonReupload.setOnClickListener {
                // 회원가입 - 학생증 제출 (step6) 화면으로 전환
                loginActivity.finish()

                val nextFragment = SignUpStep6Fragment()

                val transaction = loginActivity.supportFragmentManager.beginTransaction()
                transaction.replace(R.id.fragmentContainerView_login, nextFragment)
                transaction.commit()
                true
            }

            buttonQna.setOnClickListener {
                // 스튜딩 카카오톡 채널
                var intent = Intent(Intent.ACTION_VIEW, Uri.parse("http://pf.kakao.com/_BzmZn"))
                startActivity(intent)
            }
        }

        return binding.root
    }

    fun initView() {
        mainActivity.setStatusBarTransparent()
        mainActivity.hideBottomNavigation(true)
        
        binding.run {
            graphWaiting.run {
                imageViewSend.setImageResource(R.drawable.ic_waiting_complete)
                textViewSend.run {
                    setTextColor(resources.getColor(R.color.primary_50))
                    setTextAppearance(R.style.BodyAdd)
                }

                imageViewCheck.setImageResource(R.drawable.ic_waiting_complete)
                textViewCheck.run {
                    setTextColor(resources.getColor(R.color.primary_50))
                    setTextAppearance(R.style.BodyAdd)
                }

                imageViewComplete.setImageResource(R.drawable.ic_fail)
                textViewComplete.run {
                    setTextColor(resources.getColor(R.color.primary_50))
                    setTextAppearance(R.style.BodyAdd)
                    text = "승인 실패"
                }

                divider1.setBackgroundResource(R.color.primary_50)
                divider2.setBackgroundResource(R.color.primary_50)
            }
        }
    }
}