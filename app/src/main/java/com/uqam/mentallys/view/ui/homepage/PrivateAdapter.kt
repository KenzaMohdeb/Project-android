package com.uqam.mentallys.view.ui.homepage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.uqam.mentallys.databinding.ItemPrivateBinding
import com.uqam.mentallys.model.Private

class PrivateAdapter : ListAdapter<Private, PrivateAdapter.PrivateViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PrivateViewHolder {
        val binding = ItemPrivateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PrivateViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PrivateViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    class PrivateViewHolder(private val binding: ItemPrivateBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(private: Private) {
            binding.apply {
                privateTitle.text = private.title
                privateIcon.setImageResource(private.icon)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Private>() {
        override fun areItemsTheSame(oldItem: Private, newItem: Private) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Private, newItem: Private) =
            oldItem == newItem
    }

}