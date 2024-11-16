package com.team.studing

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.team.studing.UI.Home.HomeFailFragment
import com.team.studing.UI.Home.HomeFragment
import com.team.studing.UI.Home.HomeWaitingFragment
import com.team.studing.UI.Mypage.MypageFragment
import com.team.studing.UI.Partnership.PartnershipFragment
import com.team.studing.Utils.MyApplication.Companion.memberData
import com.team.studing.ViewModel.LoginViewModel
import com.team.studing.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private lateinit var bottomNavigationView: BottomNavigationView
    lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]

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


        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()

        Log.d("##", "member : ${memberData?.role}")
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
            }

            else -> {
                hideWriteNoticeButton(false)
                val nextFragment = HomeFragment()

                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.fragmentContainerView_main, nextFragment)
                transaction.addToBackStack(null)
                transaction.commit()
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

    fun hideBottomNavigation(state: Boolean) {
        if (state) binding.bottomNavigationView.visibility =
            View.GONE else binding.bottomNavigationView.visibility = View.VISIBLE
    }

    fun hideWriteNoticeButton(state: Boolean) {
        if (state) binding.buttonWriteNotice.visibility =
            View.GONE else binding.buttonWriteNotice.visibility = View.VISIBLE
    }
}