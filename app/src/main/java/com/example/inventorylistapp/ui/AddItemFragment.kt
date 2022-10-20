package com.example.inventorylistapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.inventorylistapp.data.InventoryApplication
import com.example.inventorylistapp.databinding.FragmentAddItemBinding
import com.example.inventorylistapp.viewmodel.InventoryViewModel
import com.example.inventorylistapp.viewmodel.InventoryViewModelFactory

class AddItemFragment : Fragment() {

    private val viewModel: InventoryViewModel by activityViewModels {
        InventoryViewModelFactory(
            (activity?.application as InventoryApplication).database
                .itemDao()
        )
    }

    private var _binding: FragmentAddItemBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAddItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnSaveData.setOnClickListener {
            addItem()
        }
    }

    private fun addItem() {
        if (validEntryCheck()) {
            viewModel.addNewItem(
                binding.etNameTxt.text.toString(),
                binding.etPriceTxt.text.toString(),
                binding.etQuantityTxt.text.toString()
            )
            val action = AddItemFragmentDirections.actionAddItemFragmentToListItemFragment()
            findNavController().navigate(action)
        }
    }

    private fun validEntryCheck(): Boolean {
        return viewModel.isEntryValid(
            binding.etNameTxt.text.toString(),
            binding.etPriceTxt.text.toString(),
            binding.etQuantityTxt.text.toString()
        )
    }

}