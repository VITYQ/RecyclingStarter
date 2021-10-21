package com.infolab.recyclingstarter.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.infolab.recyclingstarter.databinding.ActivityMainBinding
import com.infolab.recyclingstarter.view.ActionsFragment
import com.infolab.recyclingstarter.view.MainContainerFragment

class ViewPagerMainAdapter(fa: FragmentActivity, private val binding: ActivityMainBinding) : FragmentStateAdapter(fa) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        when(position){
            0 -> return MainContainerFragment()
            1 -> return ActionsFragment()
        }
        return MainContainerFragment()
    }
}