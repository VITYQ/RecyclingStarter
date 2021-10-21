package com.infolab.recyclingstarter.viewmodel

import android.app.Application
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.infolab.recyclingstarter.model.*
import com.infolab.recyclingstarter.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class RegisterViewModel(application: Application): AndroidViewModel(application) {

    private val api: RecyclerAPI = RetrofitClient.getClient().create(RecyclerAPI::class.java)
    val buildingsList = MutableLiveData<List<Building>>()
    val errorMessage = MutableLiveData<String>()
    val authenticated = MutableLiveData<Boolean>()
    private val context = getApplication<Application>().applicationContext

    init {
        fetchAllBuildings()
    }

    private fun fetchAllBuildings() {
        api.getAllBuildings().enqueue(object : Callback<List<Building>>{
            override fun onFailure(call: Call<List<Building>>, t: Throwable) {
                errorMessage.value = "Что-то пошло не так"
            }

            override fun onResponse(call: Call<List<Building>>, response: Response<List<Building>>) {
                buildingsList.value = response.body()
            }
        })
    }


    fun registerNewUser(user: UserRegister){
        api.registerUser(user).enqueue(object : Callback<String>{
            override fun onFailure(call: Call<String>, t: Throwable) {
                errorMessage.value = "Что-то пошло не так"
                Log.e("checkregister", t.localizedMessage)
            }

            @RequiresApi(Build.VERSION_CODES.N)
            override fun onResponse(call: Call<String>, response: Response<String>) {
                Log.d("checkregister", response.body().toString())
                if (response.isSuccessful){
                    authenticated.value = true
                }
                else{
                    // позволяет преобразовать тело ошибки в текст
                    val requrestMessage = if (Objects.isNull(response.errorBody())) response.message() else response.errorBody()?.string()
                    if (requrestMessage!!.contains("user with this email address already exists")){
                        errorMessage.value = "Пользователь с таким email уже существует"
                    }
                    else if (requrestMessage.contains("Enter a valid email address.")){
                        errorMessage.value = "Введите корректный email"
                    }
                    else{
                        errorMessage.value = "Что-то пошло не так, проверьте введенные данные"
                    }
                }


            }
        })
    }




}