package com.team.studing.Login

import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PixelFormat
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import com.skydoves.balloon.ArrowPositionRules
import com.skydoves.balloon.Balloon
import com.skydoves.balloon.BalloonAnimation
import com.skydoves.balloon.BalloonSizeSpec
import com.skydoves.balloon.balloon
import com.skydoves.balloon.showAlignBottom
import com.skydoves.balloon.showAlignTop
import com.team.studing.LoginActivity
import com.team.studing.R
import com.team.studing.SignUp.SignUpStep1Fragment
import com.team.studing.Utils.MainUtil.setStatusBarTransparent
import com.team.studing.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    lateinit var binding: FragmentLoginBinding
    lateinit var loginActivity: LoginActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentLoginBinding.inflate(layoutInflater)
        loginActivity = activity as LoginActivity

        binding.run {

            loginActivity.setStatusBarTransparent()

            val balloon = Balloon.Builder(loginActivity)
//                .setWidth(BalloonSizeSpec.WRAP)
                .setWidthRatio(0.6f) // sets width as 50% of the horizontal screen's size.
                .setHeight(BalloonSizeSpec.WRAP)
                .setText("스튜딩에 문의하기")
                .setTextColorResource(R.color.black_30)
                .setTextSize(12f)
                .setTextTypeface(ResourcesCompat.getFont(loginActivity,R.font.inter_regular)!!)
                .setArrowPositionRules(ArrowPositionRules.ALIGN_ANCHOR)
                .setArrowSize(10)
                .setArrowPosition(0.5f)
                .setArrowColorResource(R.color.black_5)
                .setTextGravity(Gravity.CENTER)
                .setElevation(0)
                .setPaddingHorizontal(15)
                .setPaddingVertical(5)
                .setMarginTop(10)
                .setMarginBottom(10)
                .setMarginHorizontal(35)
                .setCornerRadius(16f)
                .setBackgroundDrawableResource(R.drawable.background_tooltip_black5)
                .setBalloonAnimation(BalloonAnimation.ELASTIC)
                .build()

            buttonQna.showAlignTop(balloon)

            Handler().postDelayed({
                balloon.dismiss()
            }, 1000)

            buttonLogin.setOnClickListener {
                // 로그인 기능 구현
            }

            buttonQna.setOnClickListener {
                // 스튜딩 카카오톡 채널로 연결
                var intent = Intent(Intent.ACTION_VIEW, Uri.parse("http://pf.kakao.com/_BzmZn"))
                startActivity(intent)
            }

            buttonSignUp.setOnClickListener {
                val nextFragment = SignUpStep1Fragment()

                val transaction = loginActivity.manager.beginTransaction()
                transaction.replace(R.id.fragmentContainerView_login, nextFragment)
                transaction.addToBackStack("")
                transaction.commit()
            }
        }

        return binding.root
    }
}