package com.example.recyclingstarter.view

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.TransitionDrawable
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.example.recyclingstarter.R
import com.example.recyclingstarter.databinding.ActivityMainBinding
import com.example.recyclingstarter.model.precent
import com.example.recyclingstarter.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    lateinit var binding : ActivityMainBinding
    lateinit var ViewModel : MainViewModel
    var lastColor = Color.parseColor("#FFFFFF")
    private lateinit var animDrawable: AnimationDrawable

    var firstLaunch = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val data = precent(0)

        ViewModel = MainViewModel(data)
        binding.lifecycleOwner = this
        binding.vm = ViewModel




        ViewModel.num.observe(this, Observer {
            Log.d("viewmodelcheck", "znacheniye $it, $firstLaunch")
            changeBackgroundGradient(it)
            changeTextViewFullness(it)

        })
        firstLaunch = false
        ViewModel.startSignInActivityFlag.observe(this, Observer {
            if (it) {
                startActivity(Intent(this, SignInActivity::class.java))
                ViewModel.startSignInActivityFlag.value = false
            }

        })
    }

    override fun onStop() {
        super.onStop()
        firstLaunch = true
    }
    fun changeTextViewFullness(state: Int){
        data class text(val str: String, val color: Int)
        var str: text? = null

        when(state){
            0 -> str = text("Пуст", Color.parseColor("#37712B"))
            25 -> str = text("Немного заполнен", Color.parseColor("#92AB16"))
            50 -> str = text("Наполовину полон", Color.parseColor("#B86C14"))
            75 -> str = text("Почти полон!", Color.parseColor("#853924"))
            100 -> str = text("Заполнен!", Color.parseColor("#85244D"))
        }
        val statusBarAnimColor = ValueAnimator.ofObject(ArgbEvaluator(), lastColor, str!!.color)
        statusBarAnimColor.duration = 1000
        statusBarAnimColor.start()
        statusBarAnimColor.addUpdateListener {
            val color = it.animatedValue.toString().toInt()
            window.statusBarColor = color
            Log.d("checkcolor", color.toString())
        }
        lastColor = str.color
        binding.include.textViewFullness.text = str?.str
        binding.include.textViewFullness.setTextColor(str!!.color)
    }
    fun changeBackgroundGradient(state: Int){
        var res = 0
        when(state){
            25 -> res = R.drawable.animation_gradient_0_25
            50 -> res = R.drawable.animation_gradient_25_50
            75 -> res = R.drawable.animation_gradient_50_75
            100 -> res = R.drawable.animation_gradient_75_100
        }
        if(state == 0){ binding.mainConstraintLayout.setBackgroundResource(R.drawable.gradient_0) }
        else{
            binding.mainConstraintLayout.apply {
                setBackgroundResource(res)
                animDrawable = background as AnimationDrawable
                animDrawable.setEnterFadeDuration(10)
                animDrawable.setExitFadeDuration(1000)
                animDrawable.start()
            }
        }

    }

}