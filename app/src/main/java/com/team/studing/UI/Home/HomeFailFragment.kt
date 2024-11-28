package com.team.studing.UI.Home

//import com.team.studing.Utils.MainUtil.setStatusBarTransparent
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.team.studing.MainActivity
import com.team.studing.R
import com.team.studing.ReSubmitActivity
import com.team.studing.Utils.MainUtil.setStatusBarTransparent
import com.team.studing.databinding.FragmentHomeFailBinding

class HomeFailFragment : Fragment() {

    lateinit var binding: FragmentHomeFailBinding
    lateinit var mainActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentHomeFailBinding.inflate(layoutInflater)
        mainActivity = activity as MainActivity

        initView()

        binding.run {

            buttonReupload.setOnClickListener {
                // 학생증 재제출 화면으로 전환
                val mainIntent = Intent(mainActivity, ReSubmitActivity::class.java)
                mainIntent.putExtra("reSubmit", true)
                startActivity(mainIntent)
            }

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
        mainActivity.hideWriteNoticeButton(true)

        binding.run {
            graphWaiting.run {
                imageViewSend.setImageResource(R.drawable.ic_complete)
                textViewSend.run {
                    setTextColor(resources.getColor(R.color.primary_50))
                    setTextAppearance(R.style.BodyAdd)
                }

                imageViewCheck.setImageResource(R.drawable.ic_complete)
                textViewCheck.run {
                    setTextColor(resources.getColor(R.color.primary_50))
                    setTextAppearance(R.style.BodyAdd)
                }

                imageViewComplete.setImageResource(R.drawable.ic_fail)
                textViewComplete.run {
                    setTextColor(resources.getColor(R.color.primary_50))
                    setTextAppearance(R.style.BodyAdd)
                    text = "승인 실패"
                }

                divider1.setBackgroundResource(R.color.primary_50)
                divider2.setBackgroundResource(R.color.primary_50)
            }
        }
    }
}