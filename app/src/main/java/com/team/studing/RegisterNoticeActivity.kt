package com.team.studing

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.team.studing.UI.Register.RegisterFirstEventFragment
import com.team.studing.UI.Register.RegisterNoticeFragment
import com.team.studing.databinding.ActivityRegisterNoticeBinding

class RegisterNoticeActivity : AppCompatActivity() {

    lateinit var binding: ActivityRegisterNoticeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterNoticeBinding.inflate(layoutInflater)

        var type = intent.getStringExtra("type")
        var isEdit = intent.getBooleanExtra("isEdit", false)


        when (type) {
            "notice" -> {
                // 일반 공지
                var registerNoticeFragment = RegisterNoticeFragment()
                if (isEdit) {
                    // 데이터 전달을 위해 Bundle 생성
                    val bundle = Bundle().apply {
                        putBoolean("isEdit", isEdit)
                        putInt("id", intent.getIntExtra("id", 0))
                        putString("title", intent.getStringExtra("title"))
                        putString("content", intent.getStringExtra("content"))
                        putString("tag", intent.getStringExtra("tag"))
                        putStringArrayList("image", intent.getStringArrayListExtra("image"))
                        putString("startTime", intent.getStringExtra("startTime"))
                        putString("endTime", intent.getStringExtra("endTime"))
                    }

                    // 전달할 Fragment 생성
                    registerNoticeFragment = RegisterNoticeFragment().apply {
                        arguments = bundle // 생성한 Bundle을 Fragment의 arguments에 설정
                    }
                } else {
                    val bundle = Bundle().apply { putBoolean("isEdit", isEdit) }

                    // 전달할 Fragment 생성
                    registerNoticeFragment = RegisterNoticeFragment().apply {
                        arguments = bundle // 생성한 Bundle을 Fragment의 arguments에 설정
                    }
                }
                supportFragmentManager.beginTransaction().run {
                    replace(R.id.fragmentContainerView_register, registerNoticeFragment)
                    addToBackStack(null)
                    commit()
                }
            }

            "first event" -> {
                // 선착순 이벤트
                var registerNoticeFragment = RegisterFirstEventFragment()
                if (isEdit) {
                    // 데이터 전달을 위해 Bundle 생성
                    val bundle = Bundle().apply {
                        putBoolean("isEdit", isEdit)
                        putInt("id", intent.getIntExtra("id", 0))
                        putString("title", intent.getStringExtra("title"))
                        putString("content", intent.getStringExtra("content"))
                        putStringArrayList("image", intent.getStringArrayListExtra("image"))
                        putInt("firstEventNumber", intent.getIntExtra("firstEventNumber", 0))
                        putString("startTime", intent.getStringExtra("startTime"))
                        putString("endTime", intent.getStringExtra("endTime"))
                    }

                    // 전달할 Fragment 생성
                    registerNoticeFragment = RegisterFirstEventFragment().apply {
                        arguments = bundle // 생성한 Bundle을 Fragment의 arguments에 설정
                    }
                } else {
                    val bundle = Bundle().apply { putBoolean("isEdit", isEdit) }

                    // 전달할 Fragment 생성
                    registerNoticeFragment = RegisterFirstEventFragment().apply {
                        arguments = bundle // 생성한 Bundle을 Fragment의 arguments에 설정
                    }
                }
                supportFragmentManager.beginTransaction().run {
                    replace(R.id.fragmentContainerView_register, registerNoticeFragment)
                    addToBackStack(null)
                    commit()
                }
            }
        }

        setContentView(binding.root)
    }
}