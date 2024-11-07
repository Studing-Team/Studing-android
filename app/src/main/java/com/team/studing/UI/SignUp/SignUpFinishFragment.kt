package com.team.studing.UI.SignUp

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.team.studing.LoginActivity
import com.team.studing.MainActivity
import com.team.studing.R
import com.team.studing.Utils.MainUtil.setStatusBarTransparent
import com.team.studing.databinding.FragmentSignUpFinishBinding

class SignUpFinishFragment : Fragment() {

    lateinit var binding: FragmentSignUpFinishBinding
    lateinit var loginActivity: LoginActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSignUpFinishBinding.inflate(layoutInflater)
        loginActivity = activity as LoginActivity

        loginActivity.setStatusBarTransparent()

        binding.run {
            buttonStart.setOnClickListener {
                // 로그인 후 홈화면으로 전환

                val mainIntent = Intent(loginActivity, MainActivity::class.java)
                mainIntent.putExtra("isLogin", true)
                startActivity(mainIntent)
            }
        }

        return binding.root
    }
}