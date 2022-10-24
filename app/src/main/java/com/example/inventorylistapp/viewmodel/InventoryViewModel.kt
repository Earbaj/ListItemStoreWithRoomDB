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

    private fun updateItem(item: Item){
        viewModelScope.launch {
            itemDao.update(item)
        }
    }

    fun sellItem(item: Item) {
        if (item.quantityInStock > 0) {
            // Decrease the quantity by 1
            val newItem = item.copy(quantityInStock = item.quantityInStock - 1)
            updateItem(newItem)
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
    private fun getUpdatedItemEntry(
        itemId: Int,
        itemName: String,
        itemPrice: String,
        itemCount: String
    ): Item {
        return Item(
            id = itemId,
            itemName = itemName,
            itemPrice = itemPrice.toDouble(),
            quantityInStock = itemCount.toInt()
        )
    }
    fun updateItem(
        itemId: Int,
        itemName: String,
        itemPrice: String,
        itemCount: String
    ) {
        val updatedItem = getUpdatedItemEntry(itemId, itemName, itemPrice, itemCount)
        updateItem(updatedItem)
    }
}