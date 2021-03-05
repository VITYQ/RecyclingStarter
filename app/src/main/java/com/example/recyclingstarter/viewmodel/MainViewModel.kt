package com.example.recyclingstarter.viewmodel

import android.util.Log
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recyclingstarter.BR
import com.example.recyclingstarter.model.precent

public class MainViewModel(var data: precent) : ViewModel() {
//    val num: MutableLiveData<Int> by lazy {
//        MutableLiveData<Int>().apply { value = data.value }
//    }
    val num = MutableLiveData<Int>().apply { value = data.value }
    val startSignInActivityFlag = MutableLiveData<Boolean>().apply { value = false }
    fun logOut(){
        startSignInActivityFlag.value = true
    }
    fun increase(){
        if(num.value!! < 100){
            num.value = num.value!!.toInt() + 25
        }
    }
    fun decrease(){
        if(num.value!! > 0) {
            num.value = num.value!!.toInt() - 25
        }
    }
}