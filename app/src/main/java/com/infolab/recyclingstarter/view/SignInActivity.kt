package com.infolab.recyclingstarter.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.infolab.recyclingstarter.R
import com.infolab.recyclingstarter.databinding.ActivitySigninBinding
import com.infolab.recyclingstarter.viewmodel.SigninViewModel
import kotlinx.android.synthetic.main.activity_signin.*

class SignInActivity : AppCompatActivity() {
    lateinit var viewModel: SigninViewModel
    lateinit var binding: ActivitySigninBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_signin)
        viewModel = ViewModelProvider(this).get(SigninViewModel::class.java)
        binding.vm = viewModel
        binding.lifecycleOwner = this


        viewModel.errorMessage.observe(this, Observer {
            Toast.makeText(this, it,
                Toast.LENGTH_SHORT).show()
        })

        viewModel.authenticated.observe(this, Observer {
            if (it){
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        })

        button_signIn.setOnClickListener {
            onButtonSignInClick()

        }

        binding.textViewRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

    }

    private fun onButtonSignInClick() {
        val password = binding.editTextPassword.text.toString()
        val email = binding.editTextEmail.text.toString()
        if(password.isNullOrEmpty() || email.isNullOrEmpty()){
            Toast.makeText(this, "Нельзя оставлять поля пустыми",
                Toast.LENGTH_SHORT).show()
        }
        else{
            viewModel.signInWithEmailAndPassword(email, password)
        }

    }
}