package com.infolab.recyclingstarter.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.infolab.recyclingstarter.databinding.ItemContainerLineBinding
import com.infolab.recyclingstarter.model.Box
import com.infolab.recyclingstarter.viewmodel.MainViewModel

class RecyclerViewContainersAdapter(private val list: MutableList<Box>, private val viewModel: MainViewModel): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ContainerItem).bind(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemContainerLineBinding.inflate(inflater, parent, false)
        return ContainerItem(binding)
    }

    inner class ContainerItem(val binding: ItemContainerLineBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int){
            binding.circularIndicator.progress = list[position].fullness!!
            binding.textViewFullness.text = "${list[position].fullness}%"
            binding.textViewContainerPlace.text = "Кабинет ${list[position].room}"
            binding.textViewContainerId.text = "Контейнер ${list[position].id}"

            binding.cardViewContainer.setOnClickListener {
                viewModel.chooseBox(position)
                viewModel.currentBoxIndex.value = position
            }



            if (list[position].fullness == 0){
                binding.circularIndicator.trackColor = Color.parseColor("#CEFFCE")
            }
            else binding.circularIndicator.trackColor = Color.parseColor("#EEEEEE")



            if (list[position].fullness == 40){
                binding.circularIndicator.setIndicatorColor(Color.parseColor("#FFD94C"))
            }
            else if (list[position].fullness == 60){
                binding.circularIndicator.setIndicatorColor(Color.parseColor("#FF9441"))
            }
            else if (list[position].fullness == 80){
                binding.circularIndicator.setIndicatorColor(Color.parseColor("#FF703D"))
            }
            else if (list[position].fullness == 100){
                binding.circularIndicator.setIndicatorColor(Color.parseColor("#E15C5C"))
            }
            else {
                binding.circularIndicator.setIndicatorColor(Color.parseColor("#70BE58"))
            }

        }
    }

}