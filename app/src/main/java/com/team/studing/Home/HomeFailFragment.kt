package com.team.studing.Home

//import com.team.studing.Utils.MainUtil.setStatusBarTransparent
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.team.studing.LoginActivity
import com.team.studing.MainActivity
import com.team.studing.R
import com.team.studing.Utils.MainUtil.setStatusBarTransparent
import com.team.studing.databinding.FragmentHomeFailBinding

class HomeFailFragment : Fragment() {

    lateinit var binding: FragmentHomeFailBinding
    lateinit var mainActivity: MainActivity
    lateinit var loginActivity: LoginActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentHomeFailBinding.inflate(layoutInflater)
        mainActivity = activity as MainActivity
        loginActivity = LoginActivity()

        initView()

        binding.run {

            buttonReupload.setOnClickListener {
                // 회원가입 - 학생증 제출 (step6) 화면으로 전환
                mainActivity.finish()

                // loginActivity로 돌아가면서 추가 정보 전달
                val intent = Intent(mainActivity, LoginActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT // loginActivity를 포그라운드로 가져옴
                    putExtra("openFragment", "SignUpStep6Fragment")
                }
                mainActivity.startActivity(intent)
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