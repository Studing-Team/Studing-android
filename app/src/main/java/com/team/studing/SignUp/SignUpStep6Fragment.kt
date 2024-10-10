package com.team.studing.SignUp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import com.team.studing.LoginActivity
import com.team.studing.R
import com.team.studing.databinding.FragmentSignUpStep6Binding

class SignUpStep6Fragment : Fragment() {

    lateinit var binding: FragmentSignUpStep6Binding
    lateinit var loginActivity: LoginActivity

    var isImageUpload = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSignUpStep6Binding.inflate(layoutInflater)
        loginActivity = activity as LoginActivity

        initView()

        binding.run {
            buttonImageUpload.setOnClickListener {
                // 이미지 업로드 (갤러리)
//                isImageUpload = true
                checkComplete()
            }

            editTextStudentName.addTextChangedListener {
                checkComplete()
                if(editTextStudentName.text.isNotEmpty()) {
                    editTextStudentName.setTextAppearance(R.style.BodyAdd)
                } else {
                    editTextStudentName.setTextAppearance(R.style.Body2)
                }
            }

            editTextWholeStudentNumber.addTextChangedListener {
                checkComplete()
                if(editTextWholeStudentNumber.text.isNotEmpty()) {
                    editTextWholeStudentNumber.setTextAppearance(R.style.BodyAdd)
                } else {
                    editTextWholeStudentNumber.setTextAppearance(R.style.Body2)
                }
            }

            buttonNext.setOnClickListener {
                val nextFragment = SignUpWaitingFragment()

                val transaction = loginActivity.supportFragmentManager.beginTransaction()
                transaction.replace(R.id.fragmentContainerView_login, nextFragment)
                transaction.commit()
                true
            }

        }

        return binding.root
    }

    fun checkComplete() {
        binding.run {
            if(isImageUpload && editTextStudentName.text.isNotEmpty() && editTextWholeStudentNumber.text.isNotEmpty()) {
                buttonNext.isEnabled = true
            } else {
                buttonNext.isEnabled = false
            }
        }
    }

    fun initView() {
        binding.run {
            toolbar.run {

                textViewTitle.text = "회원가입"
                buttonBack.setOnClickListener {
                    fragmentManager?.popBackStack()
                }
            }
        }
    }
}