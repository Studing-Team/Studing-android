package com.team.studing.UI.Login

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.team.studing.LoginActivity
import com.team.studing.R
import com.team.studing.UI.SignUp.SignUpStep1Fragment
import com.team.studing.Utils.GlobalApplication.Companion.amplitude
import com.team.studing.ViewModel.LoginViewModel
import com.team.studing.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    lateinit var binding: FragmentLoginBinding
    lateinit var loginActivity: LoginActivity
    lateinit var viewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentLoginBinding.inflate(layoutInflater)
        loginActivity = activity as LoginActivity
        viewModel = ViewModelProvider(loginActivity)[LoginViewModel::class.java]

        binding.run {
            buttonLogin.setOnClickListener {
                amplitude.track("click_next_login")
                // 로그인 기능 구현
                viewModel.login(
                    loginActivity,
                    editTextId.text.toString(),
                    editTextPassword.text.toString()
                )
            }

            buttonQna.setOnClickListener {
                amplitude.track("click_contact_kakao_login")
                // 스튜딩 카카오톡 채널
                var intent = Intent(Intent.ACTION_VIEW, Uri.parse("http://pf.kakao.com/_BzmZn"))
                startActivity(intent)
            }

            buttonSignUp.setOnClickListener {
                amplitude.track("click_next_signup")

                val nextFragment = SignUpStep1Fragment()

                val transaction = loginActivity.manager.beginTransaction()
                transaction.replace(R.id.fragmentContainerView_login, nextFragment)
                transaction.addToBackStack(null)
                transaction.commit()
            }
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        initView()
    }

    fun initView() {

        binding.root.setOnTouchListener { v, event ->
            loginActivity.hideKeyboard()
            false
        }

        viewModel.getUserInfo(loginActivity)
    }
}