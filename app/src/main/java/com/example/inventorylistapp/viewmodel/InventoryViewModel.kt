package com.example.inventorylistapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventorylistapp.data.Item
import com.example.inventorylistapp.data.ItemDao
import kotlinx.coroutines.launch

class InventoryViewModel(private val itemDao: ItemDao):ViewModel() {

    private fun insertData(item: Item){
        viewModelScope.launch {
            itemDao.insert(item)
        }
    }

    private fun getNewItem(itemName: String, itemPrice: String, itemQuantity: String): Item{
        return Item(
            itemName = itemName,
            itemPrice = itemPrice.toDouble(),
            quantityInStock = itemQuantity.toInt()
        )
    }

    fun addNewItem(itemName: String, itemPrice: String, itemQuantity: String){
        val item = getNewItem(itemName,itemPrice,itemQuantity)
        insertData(item)
    }
    fun isEntryValid(itemName: String, itemPrice: String, itemCount: String): Boolean {
        if (itemName.isBlank() || itemPrice.isBlank() || itemCount.isBlank()) {
            return false
        }
        return true
    }
}