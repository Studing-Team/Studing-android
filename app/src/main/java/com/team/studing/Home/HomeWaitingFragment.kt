package com.team.studing.Home

//import com.team.studing.Utils.MainUtil.setStatusBarTransparent
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.skydoves.balloon.ArrowPositionRules
import com.skydoves.balloon.Balloon
import com.skydoves.balloon.BalloonAnimation
import com.skydoves.balloon.BalloonSizeSpec
import com.skydoves.balloon.showAlignTop
import com.team.studing.MainActivity
import com.team.studing.R
import com.team.studing.Utils.MainUtil.setStatusBarTransparent
import com.team.studing.databinding.FragmentHomeWaitingBinding

class HomeWaitingFragment : Fragment() {

    lateinit var binding: FragmentHomeWaitingBinding
    lateinit var mainActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentHomeWaitingBinding.inflate(layoutInflater)
        mainActivity = activity as MainActivity

        initView()

        binding.run {
            buttonQna.setOnClickListener {
                // 스튜딩 카카오톡 채널
                var intent = Intent(Intent.ACTION_VIEW, Uri.parse("http://pf.kakao.com/_BzmZn"))
                startActivity(intent)
            }
        }

        return binding.root
    }

    fun initView() {
        mainActivity.setStatusBarTransparent()
        mainActivity.hideBottomNavigation(true)

        binding.run {
            graphWaiting.run {
                imageViewSend.setImageResource(R.drawable.ic_complete)
                textViewSend.run {
                    setTextColor(resources.getColor(R.color.primary_50))
                    setTextAppearance(R.style.BodyAdd)
                }

                val balloon = Balloon.Builder(mainActivity)
//                .setWidth(BalloonSizeSpec.WRAP)
                    .setWidthRatio(0.8f) // sets width as % of the horizontal screen's size.
                    .setHeight(BalloonSizeSpec.WRAP)
                    .setText("24시간 이내로 승인 여부를 알려드릴게요!")
                    .setTextColorResource(R.color.black_30)
                    .setTextSize(12f)
                    .setTextTypeface(ResourcesCompat.getFont(mainActivity, R.font.inter_regular)!!)
                    .setArrowPositionRules(ArrowPositionRules.ALIGN_ANCHOR)
                    .setArrowSize(8)
                    .setArrowPosition(0.5f)
                    .setArrowColorResource(R.color.white)
                    .setTextGravity(Gravity.CENTER)
                    .setElevation(0)
                    .setPaddingHorizontal(14)
                    .setPaddingVertical(8)
                    .setMarginTop(10)
                    .setMarginBottom(4)
                    .setMarginHorizontal(35)
                    .setCornerRadius(16f)
                    .setBackgroundDrawableResource(R.drawable.background_tooltip_white)
                    .setBalloonAnimation(BalloonAnimation.ELASTIC)
                    .build()

                imageViewCheck.showAlignTop(balloon)

                imageViewCheck.setImageResource(R.drawable.ic_waiting_loading)
                textViewCheck.run {
                    setTextColor(resources.getColor(R.color.primary_50))
                    setTextAppearance(R.style.BodyAdd)
                }

                divider1.setBackgroundResource(R.color.primary_50)
            }
        }
    }

}