package com.team.studing.SignUp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.team.studing.LoginActivity
import com.team.studing.R
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

        binding.run {
            buttonStart.setOnClickListener {
                // 홈화면으로 전환
            }
        }

        return binding.root
    }
}