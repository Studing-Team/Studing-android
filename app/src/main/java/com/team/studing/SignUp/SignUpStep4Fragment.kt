package com.team.studing.SignUp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.team.studing.LoginActivity
import com.team.studing.R
import com.team.studing.databinding.FragmentSignUpStep4Binding

class SignUpStep4Fragment : Fragment() {

    lateinit var binding: FragmentSignUpStep4Binding
    lateinit var loginActivity: LoginActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSignUpStep4Binding.inflate(layoutInflater)
        loginActivity = activity as LoginActivity

        initView()

        binding.run {

            editTextStudentNumber.setOnClickListener {
                imageViewArrow.setImageResource(R.drawable.ic_up)
                editTextStudentNumber.setBackgroundResource(R.drawable.background_signup_edittext_success)

                recyclerViewStudentNumber.visibility = View.VISIBLE
            }

            buttonNext.setOnClickListener {
                val nextFragment = SignUpStep5Fragment()

                val transaction = loginActivity.supportFragmentManager.beginTransaction()
                transaction.replace(R.id.fragmentContainerView_login, nextFragment)
                transaction.commit()
                true
            }

        }

        return binding.root
    }

    fun initView() {
        binding.run {
            recyclerViewStudentNumber.visibility = View.INVISIBLE

            toolbar.run {

                textViewTitle.text = "회원가입"
                buttonBack.setOnClickListener {
                    fragmentManager?.popBackStack()
                }
            }
        }
    }
}