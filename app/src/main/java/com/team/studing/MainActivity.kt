package com.team.studing

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.team.studing.UI.Home.HomeFailFragment
import com.team.studing.UI.Home.HomeFragment
import com.team.studing.UI.Home.HomeWaitingFragment
import com.team.studing.UI.Mypage.MypageFragment
import com.team.studing.UI.Notice.NoticeDetailFragment
import com.team.studing.UI.Partnership.PartnershipFragment
import com.team.studing.Utils.GlobalApplication.Companion.amplitude
import com.team.studing.Utils.MyApplication
import com.team.studing.Utils.MyApplication.Companion.memberData
import com.team.studing.ViewModel.LoginViewModel
import com.team.studing.ViewModel.NoticeViewModel
import com.team.studing.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private lateinit var bottomNavigationView: BottomNavigationView
    lateinit var viewModel: LoginViewModel
    lateinit var noticeViewModel: NoticeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        noticeViewModel = ViewModelProvider(this)[NoticeViewModel::class.java]

        Log.d(
            "##",
            "notification type : ${MyApplication.notificationNoticeType}, ${MyApplication.notificationNoticeId}"
        )

        // 알림 데이터 처리
        initView()

        viewModel.run {
            user.observe(this@MainActivity) {
                memberData = it
                Log.d("##", "viewModel : ${it.role}")
            }
        }

        bottomNavigationView = binding.bottomNavigationView
        bottomNavigationView.itemIconTintList = null
        bottomNavigationView.setOnApplyWindowInsetsListener(null)
        bottomNavigationView.setPadding(0, 0, 0, 0)

        setBottomNavigationView()

        binding.run {
            buttonWriteNotice.setOnClickListener {
                amplitude.track("click_post_notice_home")

                val mainIntent = Intent(this@MainActivity, RegisterNoticeActivity::class.java)
                mainIntent.putExtra("register", true)
                startActivity(mainIntent)
            }
        }


        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        initView()

        if (MyApplication.reSubmit) {
            val nextFragment = HomeWaitingFragment()

            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragmentContainerView_main, nextFragment)
            transaction.commit()
        }
    }

    fun initView() {
        Log.d("##", "member : ${memberData?.role}")
        val type = intent.getStringExtra("type")
        val noticeId = intent.getStringExtra("noticeId")
        Log.d("##", "notification : ${type}, ${noticeId}")

        when (memberData?.role) {
            "ROLE_UNUSER" -> {
                hideWriteNoticeButton(true)
                hideBottomNavigation(true)
                val nextFragment = HomeWaitingFragment()

                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.fragmentContainerView_main, nextFragment)
                transaction.addToBackStack(null)
                transaction.commit()
            }

            "ROLE_DENY" -> {
                hideWriteNoticeButton(true)
                hideBottomNavigation(true)
                val nextFragment = HomeFailFragment()

                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.fragmentContainerView_main, nextFragment)
                transaction.addToBackStack(null)
                transaction.commit()
            }

            "ROLE_USER" -> {
                hideWriteNoticeButton(true)
                val nextFragment = HomeFragment()

                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.fragmentContainerView_main, nextFragment)
                transaction.addToBackStack(null)
                transaction.commit()

                if (MyApplication.notificationNoticeType == "NOTICE") {
                    MyApplication.noticeId = MyApplication.notificationNoticeId.toIntOrNull()!!
                    val nextFragment = NoticeDetailFragment()

                    val transaction = supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.fragmentContainerView_main, nextFragment)
                    transaction.addToBackStack(null)
                    transaction.commit()
                }
            }

            else -> {
                hideWriteNoticeButton(false)
                val nextFragment = HomeFragment()

                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.fragmentContainerView_main, nextFragment)
                transaction.addToBackStack(null)
                transaction.commit()

                if (MyApplication.notificationNoticeType == "NOTICE") {
                    MyApplication.noticeId = MyApplication.notificationNoticeId.toIntOrNull()!!
                    val nextFragment = NoticeDetailFragment()

                    val transaction = supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.fragmentContainerView_main, nextFragment)
                    transaction.addToBackStack(null)
                    transaction.commit()
                }
            }
        }
    }


    private fun setBottomNavigationView() {
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_home -> {
                    val nextFragment = HomeFragment()

                    val transaction = supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.fragmentContainerView_main, nextFragment)
                    transaction.addToBackStack(null)
                    transaction.commit()
                    true
                }

                R.id.menu_partnership -> {
                    val nextFragment = PartnershipFragment()

                    val transaction = supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.fragmentContainerView_main, nextFragment)
                    transaction.addToBackStack(null)
                    transaction.commit()
                    true
                }

                R.id.menu_mypage -> {
                    val nextFragment = MypageFragment()

                    val transaction = supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.fragmentContainerView_main, nextFragment)
                    transaction.addToBackStack(null)
                    transaction.commit()
                    true
                }

                else -> false
            }
        }
    }

    fun hideKeyboard() {
        val currentFocusView = currentFocus
        if (currentFocusView != null) {
            val inputManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(
                currentFocusView.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        }
    }

    fun hideBottomNavigation(state: Boolean) {
        if (state) binding.bottomNavigationView.visibility =
            View.GONE else binding.bottomNavigationView.visibility = View.VISIBLE
    }

    fun hideWriteNoticeButton(state: Boolean) {
        if (state) binding.buttonWriteNotice.visibility =
            View.GONE else binding.buttonWriteNotice.visibility = View.VISIBLE
    }
}