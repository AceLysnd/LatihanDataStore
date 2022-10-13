package com.ace.datastore.presentation

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.ace.datastore.R
import com.ace.datastore.data.AccountDataStoreManager
import com.ace.datastore.databinding.ActivityRegisterBinding
import com.ace.datastore.utils.ViewModelFactory

class RegisterActivity : AppCompatActivity() {

    private var _binding: ActivityRegisterBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: MainViewModel
    private lateinit var pref: AccountDataStoreManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        setOnclickListeners()
    }

    private fun setupViewModel() {
        pref = AccountDataStoreManager(this)
        viewModel =
            ViewModelProvider(this, ViewModelFactory(pref))[MainViewModel::class.java]
    }

    private fun setOnclickListeners() {
        binding.btnCreateAccount.setOnClickListener{
            saveAccount()
        }
    }

    private fun saveAccount() {
        if (validateInput()){
            val username = binding.etUsername.text.toString()
            val password = binding.etPassword.text.toString()

            viewModel.setAccount(username, password)

            Toast.makeText(this, "Account Created", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)

            finish()
        }

    }

    private fun validateInput(): Boolean {
        var isValid = true
        val username = binding.etUsername.text.toString()
        val password = binding.etPassword.text.toString()
        val confirmPassword = binding.etConfirmPassword.text.toString()

        if (username.isEmpty()) {
            isValid = false
            binding.etUsername.error = getString(R.string.username_is_empty)
        }
        if (password.isEmpty()) {
            isValid = false
            binding.etPassword.error = getString(R.string.password_is_empty)
        }
        if (confirmPassword.isEmpty()) {
            isValid = false
            binding.etConfirmPassword.error = getString(R.string.confirm_password)
        }
        if (password != confirmPassword) {
            isValid = false
            Toast.makeText(this, getString(R.string.password_mismatch), Toast.LENGTH_SHORT).show()
        }
        return isValid
    }
}