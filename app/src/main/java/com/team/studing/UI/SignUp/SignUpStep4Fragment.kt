package com.team.studing.UI.SignUp

import StudentNumberAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.team.studing.LoginActivity
import com.team.studing.R
import com.team.studing.Utils.MainUtil.setStatusBarTransparent
import com.team.studing.Utils.MyApplication
import com.team.studing.databinding.FragmentSignUpStep4Binding

class SignUpStep4Fragment : Fragment() {

    lateinit var binding: FragmentSignUpStep4Binding
    lateinit var loginActivity: LoginActivity

    var selectedNum = -1
    private lateinit var studentNumberAdapter: StudentNumberAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentSignUpStep4Binding.inflate(layoutInflater)
        loginActivity = activity as LoginActivity

        initView()

        binding.run {

            val studentNumbers = resources.getStringArray(R.array.student_num).toList()
            studentNumberAdapter = StudentNumberAdapter(loginActivity, studentNumbers, selectedNum)

            if (selectedNum != -1) {
                editTextStudentNumber.run {
                    text = studentNumbers[selectedNum]
                    setBackgroundResource(R.drawable.background_signup_edittext_success)
                    setTextColor(resources.getColor(R.color.black_50))
                    setTextAppearance(R.style.BodyAdd)
                }
            }

            editTextStudentNumber.setOnClickListener {
                imageViewArrow.setImageResource(R.drawable.ic_up)
                editTextStudentNumber.setBackgroundResource(R.drawable.background_signup_edittext_success)
                recyclerViewStudentNumber.visibility = View.VISIBLE

                studentNumberAdapter.run {
                    updateList(studentNumbers, selectedNum)
                    notifyDataSetChanged()

                    itemClickListener =
                        object : StudentNumberAdapter.OnItemClickListener {
                            override fun onItemClick(position: Int) {
                                selectedNum = position

                                editTextStudentNumber.run {
                                    text = studentNumbers[position]
                                    setBackgroundResource(R.drawable.background_signup_edittext_success)
                                    setTextColor(resources.getColor(R.color.black_50))
                                    setTextAppearance(R.style.BodyAdd)
                                }
                                imageViewArrow.setImageResource(R.drawable.ic_down_enabled)
                                recyclerViewStudentNumber.visibility = View.INVISIBLE
                                buttonNext.isEnabled = true
                            }
                        }
                }

                // RecyclerView 설정
                recyclerViewStudentNumber.apply {
                    layoutManager = LinearLayoutManager(context)
                    adapter = studentNumberAdapter
                }

                recyclerViewStudentNumber.visibility = View.VISIBLE
            }

            buttonNext.setOnClickListener {
                MyApplication.signUpStudentNum = editTextStudentNumber.text.toString()

                val nextFragment = SignUpStep5Fragment()

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