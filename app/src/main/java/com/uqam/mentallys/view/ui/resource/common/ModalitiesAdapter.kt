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
import com.uqam.mentallys.databinding.ItemCardBigBinding
import com.uqam.mentallys.model.resource.Modality

class ModalitiesAdapter :
    ListAdapter<Modality, ModalitiesAdapter.ViewHolder>(DiffCallback()) {

    val selectedItem: MutableLiveData<List<Modality>> =
        MutableLiveData<List<Modality>>(listOf())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemCardBigBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding, parent.context)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem, selectedItem)
    }

    class ViewHolder(
        private val binding: ItemCardBigBinding,
        private val context: Context,
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            item: Modality,
            selectedItem: MutableLiveData<List<Modality>>,
        ) {
            binding.apply {
                itemBigCardableHybridContainer.setOnClickListener {
                    if (selectedItem.value?.contains(item)!!) {
                        selectedItem.value = selectedItem.value?.minus(item)
                    } else {
                        selectedItem.value = selectedItem.value?.plus(item)
                    }
                }
                selectedItem.observeForever {
                    if (selectedItem.value?.contains(item)!!) {
                        itemBigCardableHybridContainer.setCardBackgroundColor(ContextCompat.getColor(context,R.color.colorPrimaryVariant))
                        itemBigCardableHybridName.setTextColor(ContextCompat.getColor(context,R.color.colorPrimary))
                        itemBigCardableHybridIcon.setColorFilter(ContextCompat.getColor(context,R.color.colorPrimary))
                    } else {
                        itemBigCardableHybridContainer.setCardBackgroundColor(ContextCompat.getColor(context,R.color.light_grey))
                        itemBigCardableHybridName.setTextColor(ContextCompat.getColor(context,R.color.slate))
                        itemBigCardableHybridIcon.setColorFilter(ContextCompat.getColor(context,R.color.slate))
                    }
                }
                itemBigCardableHybridName.setText(item.getTextId())
                itemBigCardableHybridIcon.setImageResource(item.getImageId())
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Modality>() {
        override fun areItemsTheSame(oldItem: Modality, newItem: Modality) =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: Modality, newItem: Modality) =
            oldItem.name == newItem.name
    }
}