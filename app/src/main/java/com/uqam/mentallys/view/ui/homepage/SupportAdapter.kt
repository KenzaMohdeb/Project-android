package com.uqam.mentallys.view.ui.homepage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.uqam.mentallys.databinding.ItemSupportBinding

import com.uqam.mentallys.model.Support

class SupportAdapter : ListAdapter<Support, SupportAdapter.SupportViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SupportViewHolder {
        val binding = ItemSupportBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SupportViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SupportViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    class SupportViewHolder(private val binding: ItemSupportBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(support: Support) {
            binding.apply {
                supportTitle.text = support.title
                supportIcon.setImageResource(support.icon)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Support>() {
        override fun areItemsTheSame(oldItem: Support, newItem: Support) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Support, newItem: Support) =
            oldItem == newItem
    }

}