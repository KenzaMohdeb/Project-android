package com.uqam.mentallys.view.ui.resource.common

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.uqam.mentallys.R
import com.uqam.mentallys.databinding.ItemCardBinding

class StringCardAdapter constructor() :
    ListAdapter<String, StringCardAdapter.ViewHolder>(DiffCallback()) {

    val selectedItem: MutableLiveData<List<String>> = MutableLiveData<List<String>>(listOf())
    private var isSelectable = false

    constructor(isSelectable: Boolean) : this() {
        this.isSelectable = isSelectable
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemCardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ViewHolder(binding, parent.context)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem, isSelectable, selectedItem)
    }

    class ViewHolder(
        private val binding: ItemCardBinding,
        val context: Context,
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: String, isSelectable: Boolean, selectedItem: MutableLiveData<List<String>>) {
            binding.apply {
                if (isSelectable) {
                    itemCardablePuretextContainer.setOnClickListener {
                        if (selectedItem.value?.contains(item)!!) {
                            selectedItem.value = selectedItem.value?.minus(item)
                        } else {
                            selectedItem.value = selectedItem.value?.plus(item)
                        }
                    }
                    selectedItem.observeForever {
                        if (selectedItem.value?.contains(item)!!) {
                            itemCardablePuretextContainer.setCardBackgroundColor(ContextCompat.getColor(context,R.color.colorPrimaryVariant))
                            itemCardablePuretextText.setTextColor(ContextCompat.getColor(context,R.color.colorPrimary))
                        } else {

                            itemCardablePuretextContainer.setCardBackgroundColor(ContextCompat.getColor(context,R.color.light_grey))
                            itemCardablePuretextText.setTextColor(ContextCompat.getColor(context,R.color.slate))
                        }
                    }
                }
                itemCardablePuretextText.text = item
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String) =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: String, newItem: String) =
            oldItem == newItem
    }
}


