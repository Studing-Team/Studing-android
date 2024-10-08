package com.team.studing.Login

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.team.studing.LoginActivity
import com.team.studing.MainActivity
import com.team.studing.R
import com.team.studing.databinding.FragmentSplashBinding

class SplashFragment : Fragment() {

    lateinit var binding: FragmentSplashBinding
    lateinit var loginActivity: LoginActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSplashBinding.inflate(layoutInflater)
        loginActivity = activity as LoginActivity

        Handler().postDelayed({
            val nextFragment = LoginFragment()

            val transaction = loginActivity.manager.beginTransaction()
            transaction.replace(R.id.fragmentContainerView_login, nextFragment)
//            transaction.addToBackStack("")
            transaction.commit()
        }, 1000)

        return binding.root
    }
}