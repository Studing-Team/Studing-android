package com.team.studing.UI.SignUp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import com.team.studing.LoginActivity
import com.team.studing.R
import com.team.studing.Utils.MainUtil.setStatusBarTransparent
import com.team.studing.databinding.FragmentSignUpStep3Binding

class SignUpStep3Fragment : Fragment() {

    lateinit var binding: FragmentSignUpStep3Binding
    lateinit var loginActivity: LoginActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignUpStep3Binding.inflate(layoutInflater)
        loginActivity = activity as LoginActivity

        initView()

        binding.run {
            editTextMajor.addTextChangedListener {
                if (editTextMajor.text.isNotEmpty()) {
                    editTextMajor.setTextAppearance(R.style.BodyAdd)
                    textViewMajorDescription.visibility = View.VISIBLE
                    recyclerViewMajor.visibility = View.VISIBLE

                    imageViewSearch.setImageResource(R.drawable.ic_delete_disabled)

                    // 검색 기능 구현

                } else {
                    editTextMajor.setTextAppearance(R.style.Body2)
                    textViewMajorDescription.visibility = View.INVISIBLE
                    recyclerViewMajor.visibility = View.INVISIBLE

                    imageViewSearch.setImageResource(R.drawable.ic_search)
                }
            }

            imageViewSearch.setOnClickListener {
                if (editTextMajor.text.isNotEmpty()) {
                    editTextMajor.setText("")
                    // editText가 비어있을 때
                    editTextMajor.setTextAppearance(R.style.Body2)
                    textViewMajorDescription.visibility = View.INVISIBLE
                    recyclerViewMajor.visibility = View.INVISIBLE

                    imageViewSearch.setImageResource(R.drawable.ic_search)

                    loginActivity.hideKeyboard()
                }
            }

            buttonRegisterMajor.setOnClickListener {
                // 우리 학교 & 학과 등록 구글폼 연결
                var intent =
                    Intent(Intent.ACTION_VIEW, Uri.parse("https://forms.gle/mjsYrF3FSeQUjnsC7"))
                startActivity(intent)
            }

            buttonNext.setOnClickListener {
                val nextFragment = SignUpStep4Fragment()

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
            textViewMajorDescription.visibility = View.INVISIBLE
            recyclerViewMajor.visibility = View.INVISIBLE
            layoutNoMajor.visibility = View.GONE
            buttonRegisterMajor.visibility = View.GONE

            toolbar.run {
                textViewTitle.text = "회원가입"
                buttonBack.setOnClickListener {
                    fragmentManager?.popBackStack()
                }
            }
        }
    }
}