package com.team.studing.UI.SignUp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.team.studing.LoginActivity
import com.team.studing.Utils.MainUtil.setStatusBarTransparent
import com.team.studing.Utils.MyApplication
import com.team.studing.ViewModel.LoginViewModel
import com.team.studing.databinding.FragmentSignUpFinishBinding

class SignUpFinishFragment : Fragment() {

    lateinit var binding: FragmentSignUpFinishBinding
    lateinit var loginActivity: LoginActivity
    lateinit var viewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentSignUpFinishBinding.inflate(layoutInflater)
        loginActivity = activity as LoginActivity
        viewModel = ViewModelProvider(loginActivity)[LoginViewModel::class.java]

        loginActivity.setStatusBarTransparent()

        binding.run {
            buttonStart.setOnClickListener {
                // 로그인 후 홈화면으로 전환
                viewModel.login(
                    loginActivity,
                    MyApplication.signUpId,
                    MyApplication.signUpPassword
                )
            }
        }

        return binding.root
    }
}