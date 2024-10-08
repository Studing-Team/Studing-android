package com.team.studing

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.team.studing.databinding.ActivityLoginBinding
import kotlin.math.log

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
}