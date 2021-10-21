package com.infolab.recyclingstarter.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.infolab.recyclingstarter.R
import com.infolab.recyclingstarter.databinding.ActivityRegisterBinding
import com.infolab.recyclingstarter.model.Building
import com.infolab.recyclingstarter.model.UserRegister
import com.infolab.recyclingstarter.viewmodel.RegisterViewModel
import com.google.android.material.snackbar.Snackbar

class RegisterActivity : AppCompatActivity() {
    lateinit var viewModel: RegisterViewModel
    lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register)
        viewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)
        binding.lifecycleOwner = this


        viewModel.authenticated.observe(this, Observer {
            Snackbar.make(binding.buttonRegister, "Ссылка для подтверждения регистрации отправлена на ваш e-mail ", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Войти", View.OnClickListener {
                        onBackPressed()
                    })
                    .show()
        })

        viewModel.errorMessage.observe(this, Observer {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        })

        viewModel.buildingsList.observe(this, Observer {
            val tmp = mutableListOf<String>()
            it.forEach {
                tmp.add(it.address!!)
            }
            val adapter = ArrayAdapter(
                this, R.layout.support_simple_spinner_dropdown_item, tmp
            )
            binding.editTextAddress.setAdapter(adapter)
            adapter.notifyDataSetChanged()
        })

        binding.buttonRegister.setOnClickListener {
            onRegisterButonClick()
        }

        binding.textViewBackToSignIn.setOnClickListener {
            onBackPressed()
        }

    }



    private fun onRegisterButonClick() {
        val name = binding.editTextName.text.toString() + " " + binding.editTextSurname.text.toString()
        val phone = binding.editTextPhone.text.toString()
        val email = binding.editTextEmail.text.toString()
        val password = binding.editTextPassword.text.toString()
        val buildingText = binding.editTextAddress.text.toString()
        val office = binding.editTextOffice.text.toString()
        val building: Building?
        if (!buildingText.isNullOrEmpty()){
            building = viewModel.buildingsList.value?.find { it.address == buildingText }!!
            Log.d("checkbuildng", building.id.toString())

            if(binding.editTextName.text.toString().isNullOrEmpty() ||
                    binding.editTextSurname.text.toString().isNullOrEmpty() ||
                    phone.isNullOrEmpty() || email.isNullOrEmpty() ||
                    password.isNullOrEmpty() || buildingText.isNullOrEmpty() || office.isNullOrEmpty()){
                Toast.makeText(this, "Нельзя оставлять поля пустыми", Toast.LENGTH_SHORT).show()
            }
            else{
                val user = UserRegister(office, email, phone, password, building.id!!, name)
                viewModel.registerNewUser(user)
            }
        }
        else{
            Toast.makeText(this, "Нельзя оставлять поля пустыми", Toast.LENGTH_SHORT).show()
        }


    }
}