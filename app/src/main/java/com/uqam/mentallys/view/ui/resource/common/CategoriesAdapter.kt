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
import com.uqam.mentallys.databinding.ItemCardImageBinding
import com.uqam.mentallys.model.resource.Category


class CategoriesAdapter :
    ListAdapter<Category, CategoriesAdapter.ViewHolder>(DiffCallback()) {

    val selectedCategory: MutableLiveData<List<Category>> =
        MutableLiveData<List<Category>>(listOf())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemCardImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding, parent.context)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem, selectedCategory)
    }

    class ViewHolder(
        private val binding: ItemCardImageBinding,
        private val context: Context,
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            item: Category,
            selectedCategory: MutableLiveData<List<Category>>,
        ) {
            binding.apply {
                itemResourceCategoryContainer.setOnClickListener {
                    if (selectedCategory.value?.size ==0 || !selectedCategory.value?.contains(item)!!) {
                        selectedCategory.value = listOf(item)
                    } else {
                        selectedCategory.value = listOf()
                    }
                }
                selectedCategory.observeForever{
                    if (selectedCategory.value?.contains(item)!! && !(selectedCategory.value?.toTypedArray() contentEquals Category.values())){
                        itemResourceCategoryContainer.setCardBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimaryVariant))
                        itemResourceCategoryText.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary))
                    } else {
                        itemResourceCategoryContainer.setCardBackgroundColor(ContextCompat.getColor(context, R.color.light_grey))
                        itemResourceCategoryText.setTextColor(ContextCompat.getColor(context, R.color.slate))
                    }
                }
                itemResourceCategoryText.setText(item.getTextIdPlural())
                itemResourceCategoryIcon.setImageResource(item.getImageId())
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Category>() {
        override fun areItemsTheSame(oldItem: Category, newItem: Category) =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: Category, newItem: Category) =
            oldItem.name == newItem.name
    }
}