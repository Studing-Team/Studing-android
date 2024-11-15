package com.team.studing.UI.SignUp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.team.studing.LoginActivity
import com.team.studing.R
import com.team.studing.Utils.MainUtil.setStatusBarTransparent
import com.team.studing.ViewModel.SignUpViewModel
import com.team.studing.databinding.FragmentSignUpWaitingBinding

class SignUpWaitingFragment : Fragment() {

    lateinit var binding: FragmentSignUpWaitingBinding
    lateinit var loginActivity: LoginActivity
    lateinit var viewModel: SignUpViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentSignUpWaitingBinding.inflate(layoutInflater)
        loginActivity = activity as LoginActivity
        viewModel = ViewModelProvider(loginActivity)[SignUpViewModel::class.java]

        initView()

        binding.run {

            buttonNotification.setOnClickListener {
                // 알림 권한 설정
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                    PackageManager.PERMISSION_DENIED == ContextCompat.checkSelfPermission(
                        loginActivity,
                        Manifest.permission.POST_NOTIFICATIONS
                    )
                ) {
                    // 푸쉬 권한 없음 -> 권한 요청
                    ActivityCompat.requestPermissions(
                        loginActivity,
                        arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                        123
                    )
                } else {
                    // 이미 권한이 있는 경우 바로 화면 전환
                    navigateToNextFragment()
                }
            }

            buttonStart.setOnClickListener {
                navigateToNextFragment()
            }

        }

        return binding.root
    }

    // Fragment에서 권한 요청 결과 처리
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 123) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 권한이 허용되었을 때 화면 전환
                navigateToNextFragment()
            } else {
                navigateToNextFragment()
            }
        }
    }

    private fun navigateToNextFragment() {
        val nextFragment = SignUpFinishFragment()

        val transaction = loginActivity.supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainerView_login, nextFragment)
        transaction.commit()
    }


    fun initView() {
        loginActivity.setStatusBarTransparent()

        binding.run {
            toolbar.run {

                textViewTitle.text = "회원가입"
                buttonBack.visibility = View.INVISIBLE
            }

            graphWaiting.run {
                imageViewSend.setImageResource(R.drawable.ic_complete)
                textViewSend.run {
                    setTextColor(resources.getColor(R.color.primary_50))
                    setTextAppearance(R.style.BodyAdd)
                }
            }
        }
    }
}