package com.team.studing.UI.SignUp

import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.team.studing.LoginActivity
import com.team.studing.R
import com.team.studing.Utils.MainUtil.setStatusBarTransparent
import com.team.studing.ViewModel.SignUpViewModel
import com.team.studing.databinding.FragmentSignUpStep1Binding

class SignUpStep1Fragment : Fragment() {

    lateinit var binding: FragmentSignUpStep1Binding
    lateinit var loginActivity: LoginActivity
    lateinit var viewModel: SignUpViewModel

    var isInit = true
    var isIdValid = false
    var isPasswordValid = false
    var isPasswordCheckValid = false

    var isPasswordShow = false
    var isPasswordCheckShow = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentSignUpStep1Binding.inflate(layoutInflater)
        loginActivity = activity as LoginActivity
        viewModel = ViewModelProvider(loginActivity)[SignUpViewModel::class.java]

        initView()

        viewModel.run {
            checkIdResult.observe(loginActivity) {
                isIdValid = it
                if (!isInit) {
                    checkIdResult()
                    checkEnable()
                }
            }
        }

        binding.run {

            buttonPasswordShow.bringToFront()

            // 아이디 정규식 확인 및 중복 확인 버튼 활성화 여부 체크
            editTextId.addTextChangedListener {
                isIdValid = false
                checkId()
                checkEnable()
            }

            editTextPassword.addTextChangedListener {
                textViewPasswordDescription.visibility = View.VISIBLE
                checkPassword()
                checkEnable()
            }

            editTextPasswordCheck.addTextChangedListener {
                textViewPasswordCheckDescription.visibility = View.VISIBLE
                checkPasswordCheck()
                checkEnable()
            }

            buttonPasswordShow.setOnClickListener {
                isPasswordShow = !isPasswordShow
                checkPasswordShow(isPasswordShow, editTextPassword, buttonPasswordShow)
                // 커서 위치를 설정
                editTextPassword.setSelection(editTextPassword.text.length)
            }

            buttonPasswordCheckShow.setOnClickListener {
                isPasswordCheckShow = !isPasswordCheckShow
                checkPasswordShow(
                    isPasswordCheckShow,
                    editTextPasswordCheck,
                    buttonPasswordCheckShow
                )
                // 커서 위치를 설정
                editTextPasswordCheck.setSelection(editTextPasswordCheck.text.length)
            }

            buttonDoubleCheck.setOnClickListener {
                // 아이디 중복 확인 (중복 확인된 경우 isIdValid = true)
                isInit = false
                viewModel.checkId(loginActivity, editTextId.text.toString())
            }

            buttonNext.setOnClickListener {
                val nextFragment = SignUpStep2Fragment()

                val transaction = loginActivity.supportFragmentManager.beginTransaction()
                transaction.add(R.id.fragmentContainerView_login, nextFragment)
                transaction.addToBackStack("")
                transaction.commit()
                true
            }
        }

        return binding.root
    }

//    override fun onResume() {
//        super.onResume()
//        isInit = false
//    }

    // 비밀번호 정규식 확인
    fun validatePassword(password: String): Boolean {
        // 정규식: 영문자, 숫자, 특수문자를 각각 1개 이상 포함 + 8자 ~ 16자 사이
        val passwordPattern =
            Regex("^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#\$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]).{8,16}$")
        return password.matches(passwordPattern)
    }

    // 아이디 정규식 확인
    fun validateId(id: String): Boolean {

        // 정규식: 영문, 숫자를 사용한 6자~12자
        val idPattern = Regex("^[a-zA-Z0-9]{6,12}$")

        return id.matches(idPattern)
    }

    fun checkEnable() {
        binding.run {
            buttonNext.isEnabled =
                isIdValid && isPasswordValid && isPasswordCheckValid
        }
    }

    // 아이디 입력시 정규식 확인 함수
    fun checkId() {
        isIdValid = false
        binding.run {
            if (validateId(editTextId.text.toString())) {
                textViewIdDescription.visibility = View.INVISIBLE
                editTextId.setBackgroundResource(R.drawable.background_signup_edittext_unselected)
                buttonDoubleCheck.isEnabled = true
            } else {
                textViewIdDescription.visibility = View.VISIBLE
                textViewIdDescription.run {
                    text = "• 영문, 숫자를 사용하여 6자~12자로 입력해 주세요"
                    setTextColor(resources.getColor(R.color.red))
                }
                editTextId.setBackgroundResource(R.drawable.background_signup_edittext_fail)
                buttonDoubleCheck.isEnabled = false
            }
        }
    }

    // 아이디 중복 확인 체크 후 호출 함수
    fun checkIdResult() {

        binding.textViewIdDescription.visibility = View.VISIBLE

        binding.run {
            if (isIdValid) {
                editTextId.setTextAppearance(R.style.BodyAdd)
                textViewIdDescription.run {
                    text = "사용 가능한 아이디에요"
                    setTextColor(resources.getColor(R.color.primary_50))
                }
                editTextId.setBackgroundResource(R.drawable.background_signup_edittext_success)
            } else {
                editTextId.setTextAppearance(R.style.Body2)
                textViewIdDescription.run {
                    text = "이미 사용 중인 아이디에요."
                    setTextColor(resources.getColor(R.color.red))
                }
                editTextId.setBackgroundResource(R.drawable.background_signup_edittext_fail)
            }
        }
    }

    fun checkPassword() {
        binding.run {
            if (editTextPassword.text.isNotEmpty()) {
                editTextPassword.setTextAppearance(R.style.BodyAdd)
                if (isPasswordShow) {
                    // 비밀번호가 보이는 경우
                    buttonPasswordShow.setImageResource(R.drawable.ic_eye_close_enabled)
                } else {
                    // 비밀번호가 보이지 않는 경우
                    buttonPasswordShow.setImageResource(R.drawable.ic_eye_open_enabled)
                }
            } else {
                editTextPassword.setTextAppearance(R.style.Body2)
                if (isPasswordShow) {
                    // 비밀번호가 보이는 경우
                    buttonPasswordShow.setImageResource(R.drawable.ic_eye_close_disabled)
                } else {
                    // 비밀번호가 보이지 않는 경우
                    buttonPasswordShow.setImageResource(R.drawable.ic_eye_open_disabled)
                }
            }
            if (validatePassword(editTextPassword.text.toString())) {
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
            checkPasswordSame()
        }
    }

    fun checkPasswordCheck() {
        binding.run {
            if (editTextPasswordCheck.text.isNotEmpty()) {
                editTextPasswordCheck.setTextAppearance(R.style.BodyAdd)
                if (isPasswordCheckShow) {
                    // 비밀번호가 보이는 경우
                    buttonPasswordCheckShow.setImageResource(R.drawable.ic_eye_close_enabled)
                } else {
                    // 비밀번호가 보이지 않는 경우
                    buttonPasswordCheckShow.setImageResource(R.drawable.ic_eye_open_enabled)
                }
            } else {
                editTextPasswordCheck.setTextAppearance(R.style.Body2)
                if (isPasswordCheckShow) {
                    // 비밀번호가 보이는 경우
                    buttonPasswordCheckShow.setImageResource(R.drawable.ic_eye_close_disabled)
                } else {
                    // 비밀번호가 보이지 않는 경우
                    buttonPasswordCheckShow.setImageResource(R.drawable.ic_eye_open_disabled)
                }
            }
            checkPasswordSame()
        }
    }

    fun checkPasswordSame() {
        binding.run {
            var isPasswordExist = editTextPassword.text.isNotEmpty()
            var isPasswordCheckExist = editTextPasswordCheck.text.isNotEmpty()

            if (isPasswordExist && isPasswordCheckExist) {
                if (editTextPasswordCheck.text.toString() == editTextPassword.text.toString()) {
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
            }
        }
    }

    fun checkPasswordShow(status: Boolean, editText: EditText, button: ImageView) {
        binding.run {
            if (status) {
                // 비밀번호 보이는 경우
                Log.d("스튜딩", "password show : ${status}")
                if (editText.text.isNotEmpty()) {
                    button.setImageResource(R.drawable.ic_eye_close_enabled)
                } else {
                    button.setImageResource(R.drawable.ic_eye_close_disabled)
                }
                editText.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
            } else {
                // 비밀번호 보이지 않는 경우
                Log.d("스튜딩", "password show : ${status}")
                if (editText.text.isNotEmpty()) {
                    button.setImageResource(R.drawable.ic_eye_open_enabled)
                } else {
                    button.setImageResource(R.drawable.ic_eye_open_disabled)
                }
                editText.transformationMethod =
                    PasswordTransformationMethod.getInstance()
            }
        }
    }

    fun initView() {
        loginActivity.setStatusBarTransparent()
        isInit = true

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