package com.team.studing.SignUp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.team.studing.LoginActivity
import com.team.studing.R
import com.team.studing.Utils.MainUtil.setStatusBarTransparent
import com.team.studing.databinding.FragmentSignUpWaitingBinding

class SignUpWaitingFragment : Fragment() {

    lateinit var binding: FragmentSignUpWaitingBinding
    lateinit var loginActivity: LoginActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSignUpWaitingBinding.inflate(layoutInflater)
        loginActivity = activity as LoginActivity

        initView()

        binding.run {

            buttonNotification.setOnClickListener {
                // 알림 권한 설정
            }

            buttonStart.setOnClickListener {
                val nextFragment = SignUpFinishFragment()

                val transaction = loginActivity.supportFragmentManager.beginTransaction()
                transaction.replace(R.id.fragmentContainerView_login, nextFragment)
                transaction.commit()
                true
            }

        }

        return binding.root
    }

    fun initView() {
        loginActivity.setStatusBarTransparent()

        binding.run {
            toolbar.run {

                textViewTitle.text = "회원가입"
                buttonBack.setOnClickListener {
                    fragmentManager?.popBackStack()
                }
            }

            graphWaiting.run {
                imageViewSend.setImageResource(R.drawable.ic_waiting_complete)
                textViewSend.run {
                    setTextColor(resources.getColor(R.color.primary_50))
                    setTextAppearance(R.style.BodyAdd)
                }
            }
        }
    }
}