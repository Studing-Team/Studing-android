package com.team.studing.SignUp

import android.os.Bundle
import android.text.InputType
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import com.team.studing.Home.HomeFragment
import com.team.studing.LoginActivity
import com.team.studing.MainActivity
import com.team.studing.R
import com.team.studing.Utils.MainUtil.setStatusBarTransparent
import com.team.studing.databinding.FragmentSignUpStep1Binding

class SignUpStep1Fragment : Fragment() {

    lateinit var binding: FragmentSignUpStep1Binding
    lateinit var loginActivity: LoginActivity

    var isIdValid = false
    var isPasswordValid = false
    var isPasswordCheckValid = false

    var isPasswordShow = false
    var isPasswordCheckShow = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSignUpStep1Binding.inflate(layoutInflater)
        loginActivity = activity as LoginActivity

        initView()

        binding.run {

            buttonPasswordShow.bringToFront()

            editTextId.addTextChangedListener {
                if(editTextId.text.isNotEmpty()) {
                    buttonDoubleCheck.isEnabled = true
                } else {
                    buttonDoubleCheck.isEnabled = false
                }
            }

            editTextPassword.addTextChangedListener {
                textViewPasswordDescription.visibility = View.VISIBLE
                if(editTextPassword.text.isNotEmpty()) {
                    editTextPasswordCheck.setTextAppearance(R.style.BodyAdd)
                    if(isPasswordShow) {
                        // 비밀번호가 보이는 경우
                        buttonPasswordShow.setImageResource(R.drawable.ic_eye_close_enabled)
                    } else {
                        // 비밀번호가 보이지 않는 경우
                        buttonPasswordShow.setImageResource(R.drawable.ic_eye_open_enabled)
                    }
                } else {
                    editTextPasswordCheck.setTextAppearance(R.style.Body2)
                    if(isPasswordShow) {
                        // 비밀번호가 보이는 경우
                        buttonPasswordShow.setImageResource(R.drawable.ic_eye_close_disabled)
                    } else {
                        // 비밀번호가 보이지 않는 경우
                        buttonPasswordShow.setImageResource(R.drawable.ic_eye_open_disabled)
                    }
                }
                if(validatePassword(editTextPassword.text.toString())) {
                    isPasswordValid = true
                    textViewPasswordDescription.run {
                        text = "사용 가능한 비밀번호에요"
                        setTextColor(resources.getColor(R.color.primary_50))
                    }
                    editTextPassword.setBackgroundResource(R.drawable.background_signup_edittext_success)
                } else {
                    isPasswordValid = false
                    textViewPasswordDescription.run {
                        text = "• 영문, 숫자, 특수문자를 각각 1개 이상 포함해요\n• 8자~16자로 입력해 주세요"
                        setTextColor(resources.getColor(R.color.red))
                    }
                    editTextPassword.setBackgroundResource(R.drawable.background_signup_edittext_fail)
                }
                if(editTextPasswordCheck.text.toString() == editTextPassword.text.toString()) {
                    isPasswordCheckValid = true
                    loginActivity.hideKeyboard()
                    textViewPasswordCheckDescription.run {
                        text = "비밀번호가 일치해요"
                        setTextColor(resources.getColor(R.color.primary_50))
                    }
                    editTextPasswordCheck.setBackgroundResource(R.drawable.background_signup_edittext_success)
                } else {
                    isPasswordCheckValid = false
                    textViewPasswordCheckDescription.run {
                        text = "비밀번호가 일치하지 않아요"
                        setTextColor(resources.getColor(R.color.red))
                    }
                    editTextPasswordCheck.setBackgroundResource(R.drawable.background_signup_edittext_fail)
                }
                checkEnable()
            }

            editTextPasswordCheck.addTextChangedListener {
                textViewPasswordCheckDescription.visibility = View.VISIBLE
                if(editTextPasswordCheck.text.isNotEmpty()) {
                    editTextPasswordCheck.setTextAppearance(R.style.BodyAdd)
                    if(isPasswordCheckShow) {
                        // 비밀번호가 보이는 경우
                        buttonPasswordCheckShow.setImageResource(R.drawable.ic_eye_close_enabled)
                    } else {
                        // 비밀번호가 보이지 않는 경우
                        buttonPasswordCheckShow.setImageResource(R.drawable.ic_eye_open_enabled)
                    }
                } else {
                    editTextPasswordCheck.setTextAppearance(R.style.Body2)
                    if(isPasswordCheckShow) {
                        // 비밀번호가 보이는 경우
                        buttonPasswordCheckShow.setImageResource(R.drawable.ic_eye_close_disabled)
                    } else {
                        // 비밀번호가 보이지 않는 경우
                        buttonPasswordCheckShow.setImageResource(R.drawable.ic_eye_open_disabled)
                    }
                }
                if(editTextPasswordCheck.text.toString() == editTextPassword.text.toString()) {
                    isPasswordCheckValid = true
                    loginActivity.hideKeyboard()
                    textViewPasswordCheckDescription.run {
                        text = "비밀번호가 일치해요"
                        setTextColor(resources.getColor(R.color.primary_50))
                    }
                    editTextPasswordCheck.setBackgroundResource(R.drawable.background_signup_edittext_success)
                } else {
                    isPasswordCheckValid = false
                    textViewPasswordCheckDescription.run {
                        text = "비밀번호가 일치하지 않아요"
                        setTextColor(resources.getColor(R.color.red))
                    }
                    editTextPasswordCheck.setBackgroundResource(R.drawable.background_signup_edittext_fail)
                }
                checkEnable()
            }

            buttonPasswordShow.setOnClickListener {
                isPasswordShow = !isPasswordShow
                if(isPasswordShow) {
                    // 비밀번호 보이는 경우
                    Log.d("스튜딩","password show : ${isPasswordShow}")
                    if(editTextPassword.text.isNotEmpty()) {
                        buttonPasswordShow.setImageResource(R.drawable.ic_eye_close_enabled)
                    } else {
                        buttonPasswordShow.setImageResource(R.drawable.ic_eye_close_disabled)
                    }
                    editTextPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                } else {
                    // 비밀번호 보이지 않는 경우
                    Log.d("스튜딩","password show : ${isPasswordShow}")
                    if(editTextPassword.text.isNotEmpty()) {
                        buttonPasswordShow.setImageResource(R.drawable.ic_eye_open_enabled)
                    } else {
                        buttonPasswordShow.setImageResource(R.drawable.ic_eye_open_disabled)
                    }
                    editTextPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                }
                // 커서 위치를 설정
                editTextPassword.setSelection(editTextPassword.text.length)
            }

            buttonPasswordCheckShow.setOnClickListener {
                isPasswordCheckShow = !isPasswordCheckShow
                if(isPasswordCheckShow) {
                    // 비밀번호 보이는 경우
                    Log.d("스튜딩","password show : ${isPasswordCheckShow}")
                    if(editTextPasswordCheck.text.isNotEmpty()) {
                        buttonPasswordCheckShow.setImageResource(R.drawable.ic_eye_close_enabled)
                    } else {
                        buttonPasswordCheckShow.setImageResource(R.drawable.ic_eye_close_disabled)
                    }
                    editTextPasswordCheck.transformationMethod = HideReturnsTransformationMethod.getInstance()
                } else {
                    // 비밀번호 보이지 않는 경우
                    Log.d("스튜딩","password show : ${isPasswordCheckShow}")
                    if(editTextPasswordCheck.text.isNotEmpty()) {
                        buttonPasswordCheckShow.setImageResource(R.drawable.ic_eye_open_enabled)
                    } else {
                        buttonPasswordCheckShow.setImageResource(R.drawable.ic_eye_open_disabled)
                    }
                    editTextPasswordCheck.transformationMethod = PasswordTransformationMethod.getInstance()
                }
                // 커서 위치를 설정
                editTextPasswordCheck.setSelection(editTextPasswordCheck.text.length)
            }

            buttonDoubleCheck.setOnClickListener {
                // 아이디 중복 확인 (중복 확인된 경우 isIdValid = true)
            }

            buttonNext.setOnClickListener {
                val nextFragment = SignUpStep2Fragment()

                val transaction = loginActivity.supportFragmentManager.beginTransaction()
                transaction.replace(R.id.fragmentContainerView_login, nextFragment)
                transaction.commit()
                true
            }
        }

        return binding.root
    }

    // 비밀번호 정규식 확인
    fun validatePassword(password: String): Boolean {
        // 정규식: 영문자, 숫자, 특수문자를 각각 1개 이상 포함 + 8자 ~ 16자 사이
        val passwordPattern = Regex("^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#\$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]).{8,16}$")
        return password.matches(passwordPattern)
    }

    fun checkEnable() {
        binding.run {
            if(isIdValid && isPasswordValid && isPasswordCheckValid) {
                buttonNext.isEnabled = true
            } else {
                buttonNext.isEnabled = false
            }
        }
    }


    fun initView() {
        loginActivity.setStatusBarTransparent()

        binding.run {
            editTextPassword.transformationMethod = PasswordTransformationMethod.getInstance()
            editTextPasswordCheck.transformationMethod = PasswordTransformationMethod.getInstance()

            textViewIdDescription.visibility = View.INVISIBLE
            textViewPasswordDescription.visibility = View.INVISIBLE
            textViewPasswordCheckDescription.visibility = View.INVISIBLE

            toolbar.run {
                textViewTitle.text = "회원가입"
                buttonClose.setOnClickListener {
                    fragmentManager?.popBackStack()
                }
            }
        }
    }
}