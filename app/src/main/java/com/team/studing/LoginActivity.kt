package com.team.studing

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.team.studing.UI.SignUp.SignUpStep6Fragment
import com.team.studing.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding
    val manager = supportFragmentManager
//    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)

//        enableEdgeToEdge()

        setContentView(binding.root)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent) // 새로운 Intent 설정

        // Intent에서 "openFragment" 값을 확인하고, SignUpStep6Fragment로 전환
        val openFragment = intent.getStringExtra("openFragment")
        if (openFragment == "SignUpStep6Fragment") {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragmentContainerView_login, SignUpStep6Fragment())
            transaction.commit()
        }
    }


    fun hideKeyboard() {
        val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(this.window.decorView.applicationWindowToken, 0)
    }
}