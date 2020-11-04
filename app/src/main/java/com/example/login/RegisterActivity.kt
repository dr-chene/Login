package com.example.login

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import com.example.login.databinding.ActivityRegisterBinding
import com.example.login.http.Http
import com.example.login.http.RequestBody
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegisterActivity : BaseActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        makeStatusBarTransparent()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register)
        initView()
        setClickListener()
    }

    private fun initView(){
        setSupportActionBar(binding.activityRegisterToolBar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeButtonEnabled(true)
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            val option = window.decorView.systemUiVisibility
            window.decorView.systemUiVisibility = option or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            window.statusBarColor = Color.parseColor("#80EEEEEE")
        }
        binding.activityRegisterEtPassword.addTextChangedListener {
            if (it != null && it.isNotEmpty()) {
                binding.activityRegisterBtnShowPassword.visibility = View.VISIBLE
            } else {
                binding.activityRegisterBtnShowPassword.visibility = View.INVISIBLE
            }
            binding.activityRegisterEtPassword.let { et ->
                et.setSelection(et.text.length)
            }
        }
        binding.activityRegisterEtConfirmPassword.addTextChangedListener {
            if (it != null && it.isNotEmpty()) {
                binding.activityRegisterBtnShowConfirmPassword.visibility = View.VISIBLE
            } else {
                binding.activityRegisterBtnShowConfirmPassword.visibility = View.INVISIBLE
            }
            binding.activityRegisterEtConfirmPassword.let { et ->
                et.setSelection(et.text.length)
            }
        }
    }

    private fun setClickListener(){
        binding.setRegisterClick {
            register()
        }

        binding.activityRegisterBtnShowPassword.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.activityRegisterEtPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
            } else {
                binding.activityRegisterEtPassword.transformationMethod = PasswordTransformationMethod.getInstance()
            }
        }
        binding.activityRegisterBtnShowConfirmPassword.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.activityRegisterEtConfirmPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
            } else {
                binding.activityRegisterEtConfirmPassword.transformationMethod = PasswordTransformationMethod.getInstance()
            }
        }
    }

    private fun register(){
        if (binding.activityRegisterEtAccount.text.toString() == ""){
            Toast.makeText(this, "用户名不能为空", Toast.LENGTH_SHORT).show()
            return
        }
        if (binding.activityRegisterEtPassword.text.toString() == ""){
            Toast.makeText(this, "密码不可为空", Toast.LENGTH_SHORT).show()
            return
        }
        if (binding.activityRegisterEtPassword.text.toString() == binding.activityRegisterEtConfirmPassword.text.toString()) {
            val map = mapOf("username" to binding.activityRegisterEtAccount.text.toString(),
                    "password" to binding.activityRegisterEtPassword.text.toString())
            val body = RequestBody("http://39.107.143.247:18080/user/register", map)
            val http = Http()
            CoroutineScope(Dispatchers.Main).launch {
                val response = http.newCall(body)
                Toast.makeText(this@RegisterActivity, response, Toast.LENGTH_SHORT).show()
                when(response){
                    "注册成功" -> registerSuccess()
                }
            }
        } else {
            Toast.makeText(this, "密码不一致，请检查", Toast.LENGTH_SHORT).show()
        }
    }

    private fun registerSuccess(){
        LoginActivity.remember(this, binding.activityRegisterEtAccount.text.toString(), binding.activityRegisterEtPassword.text.toString(), false)
        startActivity(Intent(this, HomeActivity::class.java))
    }
}