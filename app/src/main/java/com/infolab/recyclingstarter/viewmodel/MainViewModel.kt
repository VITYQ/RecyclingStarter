package com.infolab.recyclingstarter.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.infolab.recyclingstarter.model.Box
import com.infolab.recyclingstarter.model.BoxResponse
import com.infolab.recyclingstarter.model.RecyclerAPI
import com.infolab.recyclingstarter.model.User
import com.infolab.recyclingstarter.model.preferences.PreferencesProvider
import com.infolab.recyclingstarter.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

public class MainViewModel(application: Application) : AndroidViewModel(application) {


    private val api: RecyclerAPI = RetrofitClient.getClient().create(RecyclerAPI::class.java)
    private val context = getApplication<Application>().applicationContext
    var authentificated = MutableLiveData<Boolean>()
    var message = MutableLiveData<String>()
    var currentBoxIndex = MutableLiveData<Int>(). apply { value = 0 }
    var currentBox = MutableLiveData<Box>()
    var currentPage = MutableLiveData<Int>().apply { value = 0 }
    val startSignInActivityFlag = MutableLiveData<Boolean>().apply { value = false }
    val successUpload = MutableLiveData<Boolean>().apply { value = false }
    val user = MutableLiveData<User>()
    lateinit var token: String
    var id: Int = -1

    init {
        checkIsUserLoggedIn()
    }

    private fun checkIsUserLoggedIn() {
        token = PreferencesProvider(context).getUserToken()
        id = PreferencesProvider(context).getUserId()
        if(token == "Bearer null"){
            authentificated.value = false
        }
        else{
            authentificated.value = true
            fetchBoxesAndUserData()
        }

    }




    fun logOut(){
        startSignInActivityFlag.value = true
        currentPage.value = 1
    }

    // увеличиваем заполненность контейнера
    fun increase(){
        if(currentBox.value!!.fullness!! < 100){
            // обыкновенное сложение/вычитание не дает сработать observer
            user.value = user.value?.apply {
                boxes?.get(currentBoxIndex.value!!)!!.fullness =
                    user.value?.boxes?.get(currentBoxIndex.value!!)!!.fullness!! + 20
            }
        }
        val box = user.value!!.boxes?.get(currentBoxIndex.value!!)
        setContainerFullness(box!!)
    }

    // уменьшаем заполненность контейнера
    fun decrease(){
        if(currentBox.value!!.fullness!! > 0) {
            // обыкновенное сложение/вычитание не дает сработать observer
            user.value = user.value?.apply {
                boxes?.get(currentBoxIndex.value!!)!!.fullness =
                    user.value?.boxes?.get(currentBoxIndex.value!!)!!.fullness!! - 20
            }
            val box = user.value!!.boxes?.get(currentBoxIndex.value!!)
            setContainerFullness(box!!)
        }
    }

    fun chooseBox(position: Int){
        currentBox.value = user.value?.boxes?.get(position)
        currentPage.value = 0
    }

    fun fetchBoxesAndUserData(){
        api.getUserData(token, id).enqueue(object :
            Callback<User> {
            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.e("checkvm", t.localizedMessage)
            }

            override fun onResponse(
                call: Call<User>,
                response: Response<User>
            ) {
                user.value = response.body()
                // устанавливаем значение -1, чтобы viewpager не мог листать
                if (response.body()?.boxes.isNullOrEmpty()){
                    currentPage.value = -1
                }
                else{
                    currentPage.value = 0
                    currentBox.value = response.body()?.boxes?.get(0)
                }
                // при получении данных, вставляем полученные данные в настройки
                val prefProvider = PreferencesProvider(context)
                user.value?.let {
                    prefProvider.setUserSettings(it)
                }

                Log.d("checkvm", "loaded data: ${response.body()}")
            }
        })
    }


    private fun setContainerFullness(box: Box){
        api.changeFullness(token, box.id!!, BoxResponse(box.fullness, box.room)).enqueue(object : Callback<Box>{
            override fun onFailure(call: Call<Box>, t: Throwable) {
                message.value = "Произошла ошибка"
            }

            override fun onResponse(call: Call<Box>, response: Response<Box>) {
                successUpload.value = true
            }
        })
    }
}