package com.team.studing

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.team.studing.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding
    val manager = supportFragmentManager
//    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)

        enableEdgeToEdge()

//        val navHostFragment = supportFragmentManager
//            .findFragmentById(R.id.fragmentContainerView_login) as NavHostFragment
//        navController = navHostFragment.navController

        setContentView(binding.root)
    }

    fun hideKeyboard(){
        val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(this.window.decorView.applicationWindowToken, 0)
    }
}