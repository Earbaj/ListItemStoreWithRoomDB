package com.example.inventorylistapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.inventorylistapp.data.InventoryApplication
import com.example.inventorylistapp.data.Item
import com.example.inventorylistapp.databinding.FragmentAddItemBinding
import com.example.inventorylistapp.viewmodel.InventoryViewModel
import com.example.inventorylistapp.viewmodel.InventoryViewModelFactory
import kotlinx.coroutines.launch

class AddItemFragment : Fragment() {

    private val navigationArgs: AddItemFragmentArgs by navArgs()

    private val viewModel: InventoryViewModel by activityViewModels {
        InventoryViewModelFactory(
            (activity?.application as InventoryApplication).database
                .itemDao()
        )
    }

    private var _binding: FragmentAddItemBinding? = null
    private val binding get() = _binding!!

    lateinit var item: Item


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
        //Get specific id from previous fragment
        val id = navigationArgs.itemId

        if(id > 0){
            lifecycle.coroutineScope.launch {
                viewModel.retrieveItem(id).collect(){
                    item = it
                    bind(item)
                }
            }
        }else{
            binding.btnSaveData.setOnClickListener {
                addItem()
            }
        }
    }

    //Methode for add new item's to database
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

    //Check input validation's
    private fun validEntryCheck(): Boolean {
        return viewModel.isEntryValid(
            binding.etNameTxt.text.toString(),
            binding.etPriceTxt.text.toString(),
            binding.etQuantityTxt.text.toString()
        )
    }

    private fun isEntryValid(): Boolean {
        return viewModel.isEntryValid(
            binding.etNameTxt.text.toString(),
            binding.etPriceTxt.text.toString(),
            binding.etQuantityTxt.text.toString()
        )
    }

    //Bind view with the fragment
    private fun bind(item: Item){
        val price = "%.2f".format(item.itemPrice)
        binding.apply {
            etNameTxt.setText(item.itemName, TextView.BufferType.SPANNABLE)
            etPriceTxt.setText(price, TextView.BufferType.SPANNABLE)
            etQuantityTxt.setText(item.quantityInStock.toString(), TextView.BufferType.SPANNABLE)
            btnSaveData.setOnClickListener {
                updateItem()
            }
        }
    }

    //Methode for update existing item's
    private fun updateItem() {
        if (isEntryValid()) {
            viewModel.updateItem(
                this.navigationArgs.itemId,
                this.binding.etNameTxt.text.toString(),
                this.binding.etPriceTxt.text.toString(),
                this.binding.etQuantityTxt.text.toString()
            )
            val action = AddItemFragmentDirections.actionAddItemFragmentToListItemFragment()
            this.findNavController().navigate(action)
        }
    }

}