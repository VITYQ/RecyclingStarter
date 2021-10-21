package com.infolab.recyclingstarter.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.infolab.recyclingstarter.model.RecyclerAPI
import com.infolab.recyclingstarter.model.authCredentials
import com.infolab.recyclingstarter.model.authResponse
import com.infolab.recyclingstarter.model.preferences.PreferencesProvider
import com.infolab.recyclingstarter.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SigninViewModel(application: Application): AndroidViewModel(application) {

    private val api = RetrofitClient.getClient().create(RecyclerAPI::class.java)
    private val context = getApplication<Application>().applicationContext
    val errorMessage = MutableLiveData<String>()
    val authenticated = MutableLiveData<Boolean>()


    fun signInWithEmailAndPassword(email: String, password: String){
        api.authWithEmailAndPass(authCredentials(email, password)).enqueue(object : Callback<authResponse>{
            override fun onFailure(call: Call<authResponse>, t: Throwable) {
                errorMessage.value = "Что-то пошло не так"
            }

            override fun onResponse(call: Call<authResponse>, response: Response<authResponse>) {
                if (response.isSuccessful){
                    Log.d("checksignin", "response : ${response.body().toString()}")
                    PreferencesProvider(context).setUserTokenAndId(response.body()!!)
                    authenticated.value = true
                }
                else{
                    errorMessage.value = "Проверьте правильность введенных данных"
                }
            }
        })
    }
}