package com.team.studing.UI.SignUp

import MajorAdapter
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.team.studing.LoginActivity
import com.team.studing.R
import com.team.studing.Utils.MainUtil.setStatusBarTransparent
import com.team.studing.Utils.MyApplication
import com.team.studing.ViewModel.SignUpViewModel
import com.team.studing.databinding.FragmentSignUpStep3Binding

class SignUpStep3Fragment : Fragment() {

    lateinit var binding: FragmentSignUpStep3Binding
    lateinit var loginActivity: LoginActivity
    lateinit var viewModel: SignUpViewModel

    var isSelected = false

    var searchMajorList = mutableListOf<String>()
    private lateinit var majorAdapter: MajorAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignUpStep3Binding.inflate(layoutInflater)
        loginActivity = activity as LoginActivity
        viewModel = ViewModelProvider(loginActivity)[SignUpViewModel::class.java]

        initView()

        viewModel.run {
            majorList.observe(loginActivity) {
                searchMajorList = it
            }
        }

        binding.run {
            // Adapter 초기화 및 RecyclerView 설정
            majorAdapter = MajorAdapter(loginActivity, listOf(), "")
            recyclerViewMajor.adapter = majorAdapter
            recyclerViewMajor.layoutManager = LinearLayoutManager(context)

            editTextMajor.addTextChangedListener {
                isSelected = false
                buttonNext.isEnabled = false
                editTextMajor.setBackgroundResource(R.drawable.background_signup_edittext_unselected)

                val query = editTextMajor.text.toString().trim()
                if (query.isNotEmpty()) {
                    editTextMajor.setTextAppearance(R.style.BodyAdd)
                    textViewMajorDescription.visibility = View.VISIBLE
                    recyclerViewMajor.visibility = View.VISIBLE

                    imageViewSearch.setImageResource(R.drawable.ic_delete_disabled)

                    // 검색어와 일치하는 대학 목록 필터링
                    val filteredList = searchMajorList.filter { university ->
                        university.contains(query, ignoreCase = true)
                    }

                    if (filteredList.isEmpty()) {
                        recyclerViewMajor.visibility = View.INVISIBLE
                        layoutNoMajor.visibility = View.VISIBLE
                        buttonRegisterMajor.visibility = View.VISIBLE
                        buttonNext.isEnabled = false
                    } else {
                        recyclerViewMajor.visibility = View.VISIBLE
                        layoutNoMajor.visibility = View.INVISIBLE
                        buttonRegisterMajor.visibility = View.INVISIBLE

                        majorAdapter.run {
                            updateList(filteredList, query)
                            notifyDataSetChanged()

                            itemClickListener = object : MajorAdapter.OnItemClickListener {
                                override fun onItemClick(position: Int) {
                                    loginActivity.hideKeyboard()
                                    
                                    editTextMajor.run {
                                        setText(filteredList[position])
                                        setBackgroundResource(R.drawable.background_signup_edittext_success)
                                    }
                                    imageViewSearch.setImageResource(R.drawable.ic_delete_enabled)
                                    isSelected = true
                                    buttonNext.isEnabled = true
                                    recyclerViewMajor.visibility = View.INVISIBLE
                                    textViewMajorDescription.visibility = View.INVISIBLE
                                }
                            }
                        }
                    }

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
                MyApplication.signUpMajor = editTextMajor.text.toString()

                val nextFragment = SignUpStep4Fragment()

                val transaction = loginActivity.supportFragmentManager.beginTransaction()
                transaction.replace(R.id.fragmentContainerView_login, nextFragment)
                transaction.addToBackStack(null)
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