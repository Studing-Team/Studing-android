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

        when (type) {
            "notice" -> {
                // 일반 공지
                supportFragmentManager.beginTransaction().run {
                    replace(R.id.fragmentContainerView_register, RegisterNoticeFragment())
                    addToBackStack(null)
                    commit()
                }
            }

            "first event" -> {
                // 선착순 이벤트
                supportFragmentManager.beginTransaction().run {
                    replace(R.id.fragmentContainerView_register, RegisterFirstEventFragment())
                    addToBackStack(null)
                    commit()
                }
            }
        }

        setContentView(binding.root)
    }
}