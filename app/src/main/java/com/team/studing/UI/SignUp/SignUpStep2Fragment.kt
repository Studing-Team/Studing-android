package com.team.studing.UI.SignUp

import UniversityAdapter
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
import com.team.studing.databinding.FragmentSignUpStep2Binding

class SignUpStep2Fragment : Fragment() {

    lateinit var binding: FragmentSignUpStep2Binding
    lateinit var loginActivity: LoginActivity
    lateinit var viewModel: SignUpViewModel

    var isSelected = false

    var searchUniversityList = mutableListOf<String>()
    private lateinit var universityAdapter: UniversityAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentSignUpStep2Binding.inflate(layoutInflater)
        loginActivity = activity as LoginActivity
        viewModel = ViewModelProvider(loginActivity)[SignUpViewModel::class.java]

        initView()

        viewModel.run {
            universityList.observe(loginActivity) {
                searchUniversityList = it
            }
        }

        binding.run {

            // Adapter 초기화 및 RecyclerView 설정
            universityAdapter = UniversityAdapter(loginActivity, listOf(), "")
            recyclerViewUniversity.adapter = universityAdapter
            recyclerViewUniversity.layoutManager = LinearLayoutManager(context)

            scrollView.setOnTouchListener { v, event ->
                loginActivity.hideKeyboard()
                false
            }

            editTextUniversity.addTextChangedListener {
                isSelected = false
                buttonNext.isEnabled = false

                editTextUniversity.setBackgroundResource(R.drawable.background_signup_edittext_unselected)

                val query = editTextUniversity.text.toString().trim()
                if (query.isNotEmpty()) {
                    editTextUniversity.setTextAppearance(R.style.BodyAdd)
                    textViewUniversityDescription.visibility = View.VISIBLE
                    recyclerViewUniversity.visibility = View.VISIBLE

                    imageViewSearch.setImageResource(R.drawable.ic_delete_disabled)

                    // 검색어와 일치하는 대학 목록 필터링
                    val filteredList = searchUniversityList.filter { university ->
                        university.contains(query, ignoreCase = true)
                    }

                    if (filteredList.isEmpty()) {
                        recyclerViewUniversity.visibility = View.INVISIBLE
                        layoutNoUniversity.visibility = View.VISIBLE
                        buttonRegisterUniversity.visibility = View.VISIBLE
                        buttonNext.isEnabled = false
                    } else {
                        recyclerViewUniversity.visibility = View.VISIBLE
                        layoutNoUniversity.visibility = View.INVISIBLE
                        buttonRegisterUniversity.visibility = View.INVISIBLE

                        universityAdapter.run {
                            updateList(filteredList, query)
                            notifyDataSetChanged()

                            itemClickListener = object : UniversityAdapter.OnItemClickListener {
                                override fun onItemClick(position: Int) {
                                    loginActivity.hideKeyboard()

                                    editTextUniversity.run {
                                        setText(filteredList[position])
                                        setBackgroundResource(R.drawable.background_signup_edittext_success)
                                    }
                                    imageViewSearch.setImageResource(R.drawable.ic_delete_enabled)
                                    isSelected = true
                                    buttonNext.isEnabled = true
                                    recyclerViewUniversity.visibility = View.INVISIBLE
                                    textViewUniversityDescription.visibility = View.INVISIBLE
                                }
                            }
                        }
                    }

                } else {
                    editTextUniversity.setTextAppearance(R.style.Body2)
                    textViewUniversityDescription.visibility = View.INVISIBLE
                    recyclerViewUniversity.visibility = View.INVISIBLE
                    layoutNoUniversity.visibility = View.INVISIBLE

                    imageViewSearch.setImageResource(R.drawable.ic_search)
                }
            }

            imageViewSearch.setOnClickListener {
                if (editTextUniversity.text.isNotEmpty()) {
                    editTextUniversity.setText("")
                    // editText가 비어있을 때
                    editTextUniversity.setTextAppearance(R.style.Body2)
                    textViewUniversityDescription.visibility = View.INVISIBLE
                    recyclerViewUniversity.visibility = View.INVISIBLE

                    loginActivity.hideKeyboard()

                    imageViewSearch.setImageResource(R.drawable.ic_search)
                }
            }

            buttonRegisterUniversity.setOnClickListener {
                // 우리 학교 & 학과 등록 구글폼 연결
                var intent =
                    Intent(Intent.ACTION_VIEW, Uri.parse("https://forms.gle/mjsYrF3FSeQUjnsC7"))
                startActivity(intent)
            }

            buttonNext.setOnClickListener {
                MyApplication.signUpUniversity = editTextUniversity.text.toString()
                viewModel.getMajorList(loginActivity, MyApplication.signUpUniversity)

                val nextFragment = SignUpStep3Fragment()

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