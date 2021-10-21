package com.infolab.recyclingstarter.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.infolab.recyclingstarter.R
import com.infolab.recyclingstarter.adapters.RecyclerViewContainersAdapter
import com.infolab.recyclingstarter.databinding.ActionsFragmentBinding
import com.infolab.recyclingstarter.model.Box
import com.infolab.recyclingstarter.model.User
import com.infolab.recyclingstarter.viewmodel.MainViewModel

class ActionsFragment : Fragment() {

    private lateinit var adapter: RecyclerViewContainersAdapter
    private val viewModel: MainViewModel by activityViewModels()
    lateinit var binding: ActionsFragmentBinding
    lateinit var user: User

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.actions_fragment, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {

        super.onActivityCreated(savedInstanceState)
        var listBoxes = mutableListOf<Box>()
        adapter = RecyclerViewContainersAdapter(listBoxes, viewModel)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.recyclerViewContainers.adapter = adapter

        viewModel.user.observe(viewLifecycleOwner, Observer {
            if (viewModel.user.value?.boxes != null){
                listBoxes = viewModel.user.value?.boxes!!
                adapter = RecyclerViewContainersAdapter(listBoxes, viewModel)
                binding.recyclerViewContainers.adapter = adapter
            }
            else{
                Toast.makeText(requireContext(), "У вас ещё нет контейнеров", Toast.LENGTH_SHORT).show()
            }

            user = it
            binding.topAppBar.subtitle = user.room
        })

        binding.topAppBar.setNavigationOnClickListener {
            if (viewModel.currentPage.value != -1 && viewModel.currentBox.value != null){
                viewModel.currentPage.value = 0
            }
        }


        binding.topAppBar.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.menu_settings -> {
                    val intent = Intent(requireContext(), SettingsActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }

        }

    }

}