package com.example.login

import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.login.databinding.ActivityMainBinding


class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        makeStatusBarTransparent()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setClickListener()
    }

    private fun setClickListener(){
        binding.setLoginClick {
            startActivity(Intent(this, LoginActivity::class.java))
        }
        binding.setRegisterClick {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

}