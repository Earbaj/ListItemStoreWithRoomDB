package com.example.inventorylistapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.coroutineScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.inventorylistapp.adapter.ItemListAdapter
import com.example.inventorylistapp.data.InventoryApplication
import com.example.inventorylistapp.databinding.FragmentListItemBinding
import com.example.inventorylistapp.viewmodel.InventoryViewModel
import com.example.inventorylistapp.viewmodel.InventoryViewModelFactory
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ListItemFragment : Fragment() {

    private val viewModel: InventoryViewModel by activityViewModels {
        InventoryViewModelFactory(
            (activity?.application as InventoryApplication).database.itemDao()
        )
    }

    private var _binding: FragmentListItemBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentListItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ItemListAdapter{
            val action = ListItemFragmentDirections.actionListItemFragmentToDetailsItemFragment(it.id)
            this.findNavController().navigate(action)
        }
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this.context)
        lifecycle.coroutineScope.launch {
            viewModel.allItems.collect(){
                adapter.submitList(it)
            }
        }
        binding.floatingActionButton.setOnClickListener {
            val action = ListItemFragmentDirections.actionListItemFragmentToAddItemFragment("Add Item")
            findNavController().navigate(action)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}