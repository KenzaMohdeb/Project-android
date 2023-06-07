package com.uqam.mentallys.view.ui.resource.common

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.uqam.mentallys.R
import com.uqam.mentallys.databinding.ItemCardOutlinedBinding
import com.uqam.mentallys.model.interfaces.ConvertibleToCard
import com.uqam.mentallys.model.resource.Cost

class GenericOutlinedAdapter constructor() :
    ListAdapter<ConvertibleToCard, GenericOutlinedAdapter.ViewHolder>(DiffCallback()) {

    private var areIconEnabled: Boolean = true

    constructor(areIconEnabled: Boolean) : this() {
        this.areIconEnabled = areIconEnabled
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemCardOutlinedBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ViewHolder(binding, parent.context, areIconEnabled)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    class ViewHolder(
        private val binding: ItemCardOutlinedBinding,
        private val context: Context,
        private val areIconEnabled: Boolean,
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ConvertibleToCard) {
            binding.apply {
                when (item::class) {
                    Cost::class -> {
                        itemCardableHybridTitle.setText(item.getTextId())
                        val color: Int = ContextCompat.getColor(context, R.color.green)
                        itemCardableHybridTitle.setTextColor(color)
                        itemCardableHybridIcon.visibility = View.GONE
                        itemCardableHybridContainer.backgroundTintList =
                            ContextCompat.getColorStateList(context, R.color.green)
                    }
                    else -> {
                        itemCardableHybridTitle.setText(item.getTextId())
                        if (item.getImageId() != null && areIconEnabled) {
                            itemCardableHybridIcon.setImageResource(item.getImageId()!!)
                        } else {
                            itemCardableHybridIcon.visibility = View.GONE
                        }
                    }
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