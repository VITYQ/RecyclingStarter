package com.infolab.recyclingstarter.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.infolab.recyclingstarter.R
import com.infolab.recyclingstarter.adapters.ViewPagerMainAdapter
import com.infolab.recyclingstarter.databinding.ActivityMainBinding
import com.infolab.recyclingstarter.viewmodel.MainViewModel



class MainActivity : AppCompatActivity() {

    lateinit var binding : ActivityMainBinding
    lateinit var viewModel : MainViewModel


//    var firstLaunch = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        binding.vm = viewModel

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

//        binding.viewPager.setCurrentItem(0, false)

        viewModel.authentificated.observe(this, Observer {
            if(!it){
                val intent = Intent(this, SignInActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK.or(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            }
            else{
                startMainActivity()
            }
        })

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                viewModel.currentPage.value = position
            }
        })
    }

    private fun startMainActivity() {
        val viewPagerAdapter = ViewPagerMainAdapter(this, binding)
        binding.viewPager.adapter = viewPagerAdapter

        viewModel.currentPage.observe(this, Observer {
            if(viewModel.currentPage.value == -1){ // запрет листать, если нет контейнеров
                binding.viewPager.setCurrentItem(1, false)
                binding.viewPager.isUserInputEnabled = false
            }
            else{
                binding.viewPager.setCurrentItem(it, true)
                binding.viewPager.isUserInputEnabled = true
            }
        })

        viewModel.message.observe(this, Observer {
            it?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                viewModel.message.value = null
            }
        })


    }

    override fun onBackPressed() {
        if (viewModel.currentPage.value == 1 && !viewModel.user.value?.boxes.isNullOrEmpty()){
            binding.viewPager.setCurrentItem(0, true)
        }
        else
        {
            this.finish()
        }
    }


}