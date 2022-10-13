package com.ace.datastore.presentation

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.ace.datastore.R
import com.ace.datastore.data.AccountDataStoreManager
import com.ace.datastore.databinding.ActivityLoginBinding
import com.ace.datastore.utils.ViewModelFactory

class LoginActivity : AppCompatActivity() {
    private var _binding: ActivityLoginBinding? = null
    private val binding get() = _binding!!


    private lateinit var viewModel: MainViewModel
    private lateinit var pref: AccountDataStoreManager



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()

        isLoginInfoValid()
        setOnclickListeners()
    }

    private fun setupViewModel() {
        pref = AccountDataStoreManager(this)
        viewModel =
            ViewModelProvider(this, ViewModelFactory(pref))[MainViewModel::class.java]
    }

    private fun setOnclickListeners() {
        binding.btnLogin.setOnClickListener { checkLogin() }

        binding.tvGotoRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun goToHome() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }

    private fun checkLogin() {
        if (validateInput()) {
            val username = binding.etUsername.text.toString()
            val password = binding.etPassword.text.toString()

            viewModel.getAccount().observe(this) {
                if (username == it.username && password == it.password) {
                    viewModel.saveLoginInfo(true)
                    goToHome()
                } else {
                    Toast.makeText(
                        this,
                        getString(R.string.username_or_password_incorrect),
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }
        }
    }


    private fun validateInput(): Boolean {
        var isValid = true
        val username = binding.etUsername.text.toString()
        val password = binding.etPassword.text.toString()

        if (username.isEmpty()) {
            isValid = false
            binding.etUsername.error = getString(R.string.username_is_empty)
        }
        if (password.isEmpty()) {
            isValid = false
            binding.etPassword.error = getString(R.string.password_is_empty)
        }
        return isValid
    }


    private fun isLoginInfoValid() {
        viewModel.getLoginInfo().observe(this) {
            if (it){
                goToHome()
                Toast.makeText(this, "Login Verified", Toast.LENGTH_SHORT).show()
            }
        }
    }



    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}