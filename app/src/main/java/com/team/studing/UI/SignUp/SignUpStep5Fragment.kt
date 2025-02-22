package com.team.studing.UI.SignUp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.team.studing.LoginActivity
import com.team.studing.R
import com.team.studing.Utils.GlobalApplication.Companion.amplitude
import com.team.studing.Utils.MainUtil.setStatusBarTransparent
import com.team.studing.Utils.MyApplication
import com.team.studing.databinding.FragmentSignUpStep5Binding

class SignUpStep5Fragment : Fragment() {

    lateinit var binding: FragmentSignUpStep5Binding
    lateinit var loginActivity: LoginActivity

    var isAgreementAllCheck = false
    var isAgreement1Check = false
    var isAgreement2Check = false
    var isAgreement3Check = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentSignUpStep5Binding.inflate(layoutInflater)
        loginActivity = activity as LoginActivity

        initView()

        binding.run {

            textViewAgreement1.setOnClickListener {
                // 서비스 이용약관 노션 페이지
                var intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://studingofficial.notion.site/11905c1258e080ee91cecfb7ff633bab")
                )
                startActivity(intent)
            }

            textViewAgreement2.setOnClickListener {
                // 개인정보 수집 및 이용동의 노션 페이지
                var intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://studingofficial.notion.site/11905c1258e08063bba2f82d320de454")
                )
                startActivity(intent)
            }

            textViewAgreement3.setOnClickListener {
                // 마케팅 정보 수신동의 노션 페이지
                var intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://studingofficial.notion.site/12005c1258e080dbb1c2d0a2b1c1d75e")
                )
                startActivity(intent)
            }


            imageViewCheckboxAll.setOnClickListener {
                isAgreementAllCheck = !isAgreementAllCheck
                if (isAgreementAllCheck) {
                    isAgreement1Check = true
                    isAgreement2Check = true
                    isAgreement3Check = true
                } else {
                    isAgreement1Check = false
                    isAgreement2Check = false
                    isAgreement3Check = false
                }
                checkAgreement()
                if (isAgreementAllCheck) {
                    imageViewCheckboxAll.setImageResource(R.drawable.ic_checkbox_checked)
                    imageViewCheckbox1.setImageResource(R.drawable.ic_checkbox_checked)
                    imageViewCheckbox2.setImageResource(R.drawable.ic_checkbox_checked)
                    imageViewCheckbox3.setImageResource(R.drawable.ic_checkbox_checked)
                } else {
                    imageViewCheckboxAll.setImageResource(R.drawable.ic_checkbox_unchecked)
                    imageViewCheckbox1.setImageResource(R.drawable.ic_checkbox_unchecked)
                    imageViewCheckbox2.setImageResource(R.drawable.ic_checkbox_unchecked)
                    imageViewCheckbox3.setImageResource(R.drawable.ic_checkbox_unchecked)
                }
            }

            imageViewCheckbox1.setOnClickListener {
                isAgreement1Check = !isAgreement1Check
                checkAgreement()
                checkALlAgreement()
                if (isAgreement1Check) {
                    imageViewCheckbox1.setImageResource(R.drawable.ic_checkbox_checked)
                } else {
                    imageViewCheckbox1.setImageResource(R.drawable.ic_checkbox_unchecked)
                }
            }

            imageViewCheckbox2.setOnClickListener {
                isAgreement2Check = !isAgreement2Check
                checkAgreement()
                checkALlAgreement()
                if (isAgreement2Check) {
                    imageViewCheckbox2.setImageResource(R.drawable.ic_checkbox_checked)
                } else {
                    imageViewCheckbox2.setImageResource(R.drawable.ic_checkbox_unchecked)
                }
            }

            imageViewCheckbox3.setOnClickListener {
                isAgreement3Check = !isAgreement3Check
                checkAgreement()
                checkALlAgreement()
                if (isAgreement3Check) {
                    imageViewCheckbox3.setImageResource(R.drawable.ic_checkbox_checked)
                } else {
                    imageViewCheckbox3.setImageResource(R.drawable.ic_checkbox_unchecked)
                }
            }

            textViewAgreement1.setOnClickListener {
                // 서비스 이용약관 노션 페이지 연결
                var intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://studingofficial.notion.site/11905c1258e080ee91cecfb7ff633bab")
                )
                startActivity(intent)
            }

            textViewAgreement2.setOnClickListener {
                // 개인정보 수집 및 이용 동의 노션 페이지 연결
                var intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://studingofficial.notion.site/11905c1258e08063bba2f82d320de454")
                )
                startActivity(intent)
            }

            textViewAgreement3.setOnClickListener {
                // 마케팅 정보 수신 동의 노션 페이지 연결
                var intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://studingofficial.notion.site/12005c1258e080dbb1c2d0a2b1c1d75e")
                )
                startActivity(intent)
            }

            buttonNext.setOnClickListener {
                amplitude.track("click_next_signup_step5")

                MyApplication.marketingAgreement = isAgreement3Check.toString()

                val nextFragment = SignUpStep6Fragment()

                val transaction = loginActivity.supportFragmentManager.beginTransaction()
                transaction.replace(R.id.fragmentContainerView_login, nextFragment)
                transaction.addToBackStack(null)
                transaction.commit()
            }
        }

        return binding.root
    }

    fun checkAgreement() {
        binding.run {
            buttonNext.isEnabled = isAgreement1Check && isAgreement2Check
        }
    }

    fun checkALlAgreement() {
        binding.run {
            if (isAgreement1Check && isAgreement2Check && isAgreement3Check) {
                imageViewCheckboxAll.setImageResource(R.drawable.ic_checkbox_checked)
            } else {
                imageViewCheckboxAll.setImageResource(R.drawable.ic_checkbox_unchecked)
            }
        }
    }

    fun initView() {
        loginActivity.setStatusBarTransparent()

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