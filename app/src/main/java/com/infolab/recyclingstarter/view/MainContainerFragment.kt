package com.infolab.recyclingstarter.view

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.infolab.recyclingstarter.R
import com.infolab.recyclingstarter.databinding.MainContainerFragmentBinding
import com.infolab.recyclingstarter.viewmodel.MainViewModel

class MainContainerFragment() : Fragment() {

    private lateinit var binding: MainContainerFragmentBinding
    private val viewModel: MainViewModel by activityViewModels()
    var lastColor = Color.parseColor("#FFFFFF")
    var firstView = true
    var timerIsStarted = false
    private lateinit var timer : CountDownTimer

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.main_container_fragment, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.vm = viewModel
        binding.lifecycleOwner = viewLifecycleOwner


        binding.include.buttonDecrease.setOnClickListener {
            viewModel.decrease()
            showUploadIndicator()
        }

        binding.include.buttonIncrease.setOnClickListener {
            viewModel.increase()
            showUploadIndicator()
        }

        viewModel.currentBox.observe(requireActivity(), Observer {
            if (it != null){
                val state = it.fullness!!
                updateUi(state)
            }
        })

        viewModel.currentPage.observe(requireActivity(), Observer {
            Log.d("checkpager", it.toString())
            Log.d("checkpager", viewModel.currentBox.value.toString())
            if (it == 0 && viewModel.currentBox.value != null){
                val state = viewModel.currentBox.value!!.fullness!!
                updateUi(state)
            }
            else if (viewModel.currentBox.value != null){
                changeStatusBarColor(-1)
            }
        })

        viewModel.successUpload.observe(requireActivity(), Observer {
            if (it){
                onSuccessUpload()
            }
        })

        viewModel.user.observe(requireActivity(), Observer {
            if (viewModel.currentBox.value != null){
                val box = it.boxes?.get(viewModel.currentBoxIndex.value!!)
                val state = box?.fullness!!
                updateUi(state)
            }
            else if (it.boxes != null && it.boxes.size != 0){
                val state = it.boxes[0].fullness!!
                updateUi(state)
            }

        })


    }

    private fun updateUi(state: Int) {
        // меняем текст карточки
        changeTextViewFullness(state)

        // меняем цвет фона
        changeBackgroundGradient(state)

        // меняем цвет шторки
        changeStatusBarColor(state)
    }

    private fun onSuccessUpload() {
        if (timerIsStarted){
            timerIsStarted = false
            timer.cancel()
        }
        binding.textViewSuccess.visibility = View.VISIBLE
        binding.circularIndicatior.visibility = View.INVISIBLE
        hideUploadIndicatior()
        viewModel.successUpload.value = false

    }

    private fun showUploadIndicator() {
        binding.cardViewIndicatior.alpha = 0f
        binding.cardViewIndicatior.visibility = View.VISIBLE
        binding.textViewSuccess.visibility = View.GONE
        binding.circularIndicatior.visibility = View.VISIBLE
        val a = binding.cardViewIndicatior.animate()
                .alpha(1f)
                .setDuration(200)
                .start()

    }

    private fun hideUploadIndicatior() {
        timer = object : CountDownTimer(2000,2000){
            override fun onTick(millisUntilFinished: Long) {

            }

            override fun onFinish() {
                binding.cardViewIndicatior.animate()
                        .alpha(0f)
                        .setDuration(200)
                        .start()
            }
        }
        timer.start()
        timerIsStarted = true
    }

    fun changeTextViewFullness(state: Int){
        data class text(val str: String, val color: Int)
        var str: text? = null

        when(state){
            0 -> str = text("Пуст", Color.parseColor("#A1A1A1"))
            20 -> str = text("Немного заполнен", Color.parseColor("#70BE58"))
            40 -> str = text("Наполовину полон", Color.parseColor("#D4B044"))
            60 -> str = text("Значительно заполнен", Color.parseColor("#ED883C"))
            80 -> str = text("Почти заполнен", Color.parseColor("#E15C5C"))
            100 -> str = text("Заполнен!", Color.parseColor("#D36537"))
        }

        binding.include.textViewFullness.text = str?.str
        binding.include.textViewFullness.setTextColor(str!!.color)



    }

    private fun changeStatusBarColor(state: Int) {
        var color: Int? = null
        when(state){
            -1 -> color = resources.getColor(R.color.green_400)
            0 -> color = Color.parseColor("#98D251")
            20 -> color = Color.parseColor("#98D251")
            40 -> color = Color.parseColor("#E0E019")
            60 -> color = Color.parseColor("#FFD94C")
            80 -> color = Color.parseColor("#FF9441")
            100 -> color = Color.parseColor("#E15C5C")

        }
        val statusBarAnimColor = ValueAnimator.ofObject(ArgbEvaluator(), lastColor, color)
        statusBarAnimColor.duration = 1000
        statusBarAnimColor.start()
        statusBarAnimColor.addUpdateListener {
            val color = it.animatedValue.toString().toInt()
            activity?.window?.statusBarColor = color
        }
        lastColor = color!!
    }


    @SuppressLint("UseCompatLoadingForDrawables")
    fun changeBackgroundGradient(state: Int){

        var src: Drawable? = null

        when(state){
            0 -> src = resources.getDrawable(R.drawable.gradient_0)
            20 -> src = resources.getDrawable(R.drawable.gradient_0)
            40 -> src = resources.getDrawable(R.drawable.gradient_25)
            60 -> src = resources.getDrawable(R.drawable.gradient_50)
            80 -> src = resources.getDrawable(R.drawable.gradient_75)
            100 -> src = resources.getDrawable(R.drawable.gradient_100)
        }

        if (firstView){
            binding.imageEnd.setImageDrawable(src)
            firstView = !firstView
        }
        else{
            binding.imageStart.setImageDrawable(src)
            firstView = !firstView
        }

        val inAnim = AlphaAnimation(0f, 1f).apply { duration = 1000 }
        val outAnim = AlphaAnimation(1f, 0f).apply { duration = 1000 }

        binding.viewSwitcher.inAnimation = inAnim
        binding.viewSwitcher.outAnimation = outAnim

        binding.viewSwitcher.showNext()
    }
}