package com.example.inventorylistapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.coroutineScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.inventorylistapp.data.InventoryApplication
import com.example.inventorylistapp.data.Item
import com.example.inventorylistapp.data.getFormattedPrice
import com.example.inventorylistapp.databinding.FragmentDetailsItemBinding
import com.example.inventorylistapp.viewmodel.InventoryViewModel
import com.example.inventorylistapp.viewmodel.InventoryViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch


class DetailsItemFragment : Fragment() {

    private val viewModel: InventoryViewModel by activityViewModels {
        InventoryViewModelFactory(
            (activity?.application as InventoryApplication).database.itemDao()
        )
    }

    lateinit var item: Item
    private val navigationArgs: DetailsItemFragmentArgs by navArgs()

    private var _binding: FragmentDetailsItemBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentDetailsItemBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = navigationArgs.id
        lifecycle.coroutineScope.launch {
            viewModel.retrieveItem(id).collect(){
                item = it
                bind(item)
            }
        }
    }

    private fun bind(item: Item){
        binding.apply {
            txtName.text = item.itemName
            txtPrice.text = item.getFormattedPrice()
            txtQuantity.text = item.quantityInStock.toString()
            btnDelete.setOnClickListener {
                showConfirmationDialog()
            }
            btnSell.setOnClickListener { viewModel.sellItem(item) }
        }
    }

    private fun deleteItem() {
        viewModel.delete(item)
        findNavController().navigateUp()
    }

    private fun showConfirmationDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(android.R.string.dialog_alert_title))
            .setMessage("Are you want to delete this data")
            .setCancelable(false)
            .setNegativeButton("No") { _, _ -> }
            .setPositiveButton("Yes") { _, _ ->
                deleteItem()
            }
            .show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}