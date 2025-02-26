package com.team.studing

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.messaging.FirebaseMessaging
import com.team.studing.UI.SignUp.SignUpStep6Fragment
import com.team.studing.Utils.MyApplication
import com.team.studing.Utils.PreferenceUtil
import com.team.studing.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding
    val manager = supportFragmentManager
    lateinit var sharedPreferenceManager: PreferenceUtil

    lateinit var imm: InputMethodManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        MyApplication.preferences = PreferenceUtil(applicationContext)

        setFCMToken()
        handleNotificationIntent(intent)

        setContentView(binding.root)
    }

    private fun handleNotificationIntent(intent: Intent) {
        MyApplication.notificationNoticeType = intent.getStringExtra("type").toString()
        MyApplication.notificationNoticeId = intent.getStringExtra("noticeId").toString()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent) // 새로운 Intent 설정

        intent.let { handleNotificationIntent(it) } // 앱 실행 중 알림 클릭 처리

        // Intent에서 "openFragment" 값을 확인하고, SignUpStep6Fragment로 전환
        val openFragment = intent.getStringExtra("openFragment")
        if (openFragment == "SignUpStep6Fragment") {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragmentContainerView_login, SignUpStep6Fragment())
            transaction.commit()
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val fragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView_login)
        fragment?.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    fun setFCMToken() {

        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.d("FCM Token", "Fetching FCM registration token failed", task.exception)
                return@addOnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result
            Log.d("FCM Token", "$token")
            MyApplication.preferences.setFCMToken(token)
            Log.d("FCM Token", "FCM 토큰 : ${MyApplication.preferences.getFCMToken()}")

            if (this::sharedPreferenceManager.isInitialized) {
                Log.d("FCM Token", "this::sharedPreferenceManager.isInitialized")
                sharedPreferenceManager.setFCMToken(token)
            }
        }
    }
}