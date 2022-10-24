package com.example.inventorylistapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventorylistapp.data.Item
import com.example.inventorylistapp.data.ItemDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class InventoryViewModel(private val itemDao: ItemDao):ViewModel() {

    val allItems: Flow<List<Item>> = itemDao.getItems()

    fun retrieveItem(id: Int): Flow<Item> {
        return itemDao.getItem(id)
    }

    private fun insertData(item: Item){
        viewModelScope.launch {
            itemDao.insert(item)
        }
    }

    private fun deleteItem(item: Item){
        viewModelScope.launch {
            itemDao.delete(item)
        }
    }

    fun delete(item: Item){
        deleteItem(item)
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