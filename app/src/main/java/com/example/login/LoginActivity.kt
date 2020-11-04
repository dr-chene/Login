package com.example.login

import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.util.Pair
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import com.example.login.bean.User
import com.example.login.databinding.ActivityLoginBinding
import com.example.login.http.Http
import com.example.login.http.RequestBody
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : BaseActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        makeStatusBarTransparent()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        initView()
        setClickListener()
    }

    private fun initView(){
        setSupportActionBar(binding.activityLoginToolBar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeButtonEnabled(true)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val option = window.decorView.systemUiVisibility
            window.decorView.systemUiVisibility = option or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            window.statusBarColor = Color.parseColor("#80EEEEEE")
        }
        binding.activityLoginEtPassword.addTextChangedListener {
            if (it != null && it.isNotEmpty()) {
                binding.activityLoginBtnShowPassword.visibility = View.VISIBLE
            } else {
                binding.activityLoginBtnShowPassword.visibility = View.INVISIBLE
            }
            binding.activityLoginEtPassword.let { et ->
                et.setSelection(et.text.length)
            }
        }
        load()
    }

    private fun setClickListener(){
        binding.setLoginClick {
            login()
        }
        binding.setRegisterClick {
            val options = ActivityOptions.makeSceneTransitionAnimation(this,
                    Pair.create(binding.activityLoginEtAccount, "account"),
                    Pair.create(binding.activityLoginEtPassword, "password"),
                    Pair.create(binding.activityLoginBtn, "btn"))
            startActivity(Intent(this, RegisterActivity::class.java), options.toBundle())
        }
        binding.activityLoginBtnShowPassword.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.activityLoginEtPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
            } else {
                binding.activityLoginEtPassword.transformationMethod = PasswordTransformationMethod.getInstance()
            }
        }
    }

    private fun login(){
        val map = mapOf("username" to binding.activityLoginEtAccount.text.toString(), "password" to binding.activityLoginEtPassword.text.toString())
        val body = RequestBody("http://39.107.143.247:18080/user/login", map);
        val http = Http()
        CoroutineScope(Dispatchers.Main).launch {
            val response = http.newCall(body)
            Toast.makeText(this@LoginActivity, response, Toast.LENGTH_SHORT).show()
            when(response){
                "登陆成功" -> loginSuccess()
            }
        }
    }

    private fun loginSuccess(){
            remember(this, binding.activityLoginEtAccount.text.toString(), binding.activityLoginEtPassword.text.toString(), binding.activityLoginRememberCheckbox.isChecked)
            startActivity(Intent(this, HomeActivity::class.java))
    }

    private fun load(){
        getSharedPreferences(PREFS_NAME, MODE_PRIVATE).apply {
            val username = getString("username", "")
            val password = getString("password", "")
            val remember = getBoolean("remember", false)
            if (!username.equals("")){
                binding.activityLoginEtAccount.setText(username)
            }
            if (remember){
                binding.activityLoginEtPassword.setText(password)
            }
            binding.activityLoginRememberCheckbox.isChecked = remember
        }
    }

    companion object {
        const val PREFS_NAME = "user"

        fun remember(context: Context, username: String, password: String, isRemember: Boolean){
            val sp = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
            sp.edit().apply{
                putString("username", username)
                putString("password", password)
                putBoolean("remember", isRemember)
                apply()
            }
        }
    }
}