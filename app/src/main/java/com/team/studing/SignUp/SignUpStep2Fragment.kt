package com.team.studing.SignUp

import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import com.team.studing.LoginActivity
import com.team.studing.R
import com.team.studing.databinding.FragmentSignUpStep2Binding

class SignUpStep2Fragment : Fragment() {

    lateinit var binding: FragmentSignUpStep2Binding
    lateinit var loginActivity: LoginActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSignUpStep2Binding.inflate(layoutInflater)
        loginActivity = activity as LoginActivity

        initView()

        binding.run {

            editTextUniversity.addTextChangedListener {
                if(editTextUniversity.text.isNotEmpty()) {
                    editTextUniversity.setTextAppearance(R.style.BodyAdd)
                    textViewUniversityDescription.visibility = View.VISIBLE
                    recyclerViewUniversity.visibility = View.VISIBLE

                    imageViewSearch.setBackgroundResource(R.drawable.ic_delete_disabled)

                    // 검색 기능 구현

                } else {
                    editTextUniversity.setTextAppearance(R.style.Body2)
                    textViewUniversityDescription.visibility = View.INVISIBLE
                    recyclerViewUniversity.visibility = View.INVISIBLE

                    imageViewSearch.setBackgroundResource(R.drawable.ic_search)
                }
            }

            imageViewSearch.setOnClickListener {
                if(editTextUniversity.text.isNotEmpty()) {
                    editTextUniversity.setText("")
                    // editText가 비어있을 때
                    editTextUniversity.setTextAppearance(R.style.Body2)
                    textViewUniversityDescription.visibility = View.INVISIBLE
                    recyclerViewUniversity.visibility = View.INVISIBLE

                    imageViewSearch.setBackgroundResource(R.drawable.ic_search)

                    loginActivity.hideKeyboard()
                }
            }

            buttonNext.setOnClickListener {
                val nextFragment = SignUpStep3Fragment()

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
            textViewUniversityDescription.visibility = View.INVISIBLE
            recyclerViewUniversity.visibility = View.INVISIBLE
            layoutNoUniversity.visibility = View.GONE
            buttonRegisterUniversity.visibility = View.GONE

            toolbar.run {
                textViewTitle.text = "회원가입"
                buttonBack.setOnClickListener {
                    fragmentManager?.popBackStack()
                }
            }
        }
    }

}