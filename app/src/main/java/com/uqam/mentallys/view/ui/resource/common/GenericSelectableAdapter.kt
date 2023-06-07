package com.uqam.mentallys.view.ui.resource.common

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.uqam.mentallys.databinding.ItemSelectableBinding
import com.uqam.mentallys.model.interfaces.ConvertibleToCard

class GenericSelectableAdapter :
    ListAdapter<ConvertibleToCard, GenericSelectableAdapter.ViewHolder>(DiffCallback()) {

    val selectedItem: MutableLiveData<List<ConvertibleToCard>> = MutableLiveData<List<ConvertibleToCard>>(listOf())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemSelectableBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ViewHolder(binding, parent.context)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem, selectedItem)
    }

    class ViewHolder(
        private val binding: ItemSelectableBinding,
        private val context: Context,
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ConvertibleToCard, selectedItem: MutableLiveData<List<ConvertibleToCard>>) {
            binding.apply {
                itemSelectableName.text = context.getText(item.getTextId())
                itemSelectableContainer.setOnClickListener{
                    if (selectedItem.value?.contains(item)!!) {
                        selectedItem.value = selectedItem.value?.minus(item)
                    } else {
                        selectedItem.value = selectedItem.value?.plus(item)
                    }
                }
                itemSelectableCheckbox.setOnClickListener {
                    if (selectedItem.value?.contains(item)!!) {
                        selectedItem.value = selectedItem.value?.minus(item)
                    } else {
                        selectedItem.value = selectedItem.value?.plus(item)
                    }
                }
                selectedItem.observeForever {
                    itemSelectableCheckbox.isChecked = selectedItem.value?.contains(item)!!
                }
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<ConvertibleToCard>() {
        override fun areItemsTheSame(oldItem: ConvertibleToCard, newItem: ConvertibleToCard) =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: ConvertibleToCard, newItem: ConvertibleToCard) =
            oldItem.getTextId() == newItem.getTextId()
    }
}