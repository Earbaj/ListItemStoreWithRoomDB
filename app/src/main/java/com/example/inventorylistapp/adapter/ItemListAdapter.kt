package com.example.inventorylistapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.inventorylistapp.data.Item
import com.example.inventorylistapp.data.ItemDao
import com.example.inventorylistapp.data.getFormattedPrice
import com.example.inventorylistapp.databinding.FragmentListItemBinding
import com.example.inventorylistapp.databinding.ListItemBinding

class ItemListAdapter(private val onItemClick: (Item) -> Unit)
    : ListAdapter<Item, ItemListAdapter.ItemViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            ListItemBinding.inflate(
                LayoutInflater.from(
                    parent.context
                )
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val current = getItem(position)
        holder.itemView.setOnClickListener {
            onItemClick(current)
        }
        holder.bind(current)
    }

    class ItemViewHolder(private var binding: ListItemBinding)
        :RecyclerView.ViewHolder(binding.root){
            fun bind(item: Item){
                binding.apply {
                    itemName.text = item.itemName
                    itemPrice.text = item.getFormattedPrice()
                    itemQuantity.text = item.quantityInStock.toString()
                }
            }
        }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Item>() {
            override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
                return oldItem.itemName == newItem.itemName
            }
        }
    }


}