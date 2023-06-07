package com.uqam.mentallys.view.ui.resource.common

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.uqam.mentallys.R
import com.uqam.mentallys.databinding.ItemHourBinding


class HourAdapter :
    ListAdapter<Pair<Pair<Int, Int>, Pair<Int, Int>>, HourAdapter.ViewHolder>(DiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemHourBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding, parent.context)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    class ViewHolder(
        private val binding: ItemHourBinding,
        private val context: Context,
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Pair<Pair<Int, Int>, Pair<Int, Int>>) {
            binding.apply {
                val hour1 = "${item.first.first}:${item.first.second}"
                val hour2 = "${item.second.first}:${item.second.second}"
                val text =
                    "${context.getText(R.string.resource_from)} $hour1 ${context.getText(R.string.resource_to)} $hour2"
                itemHourText.text = text
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Pair<Pair<Int, Int>, Pair<Int, Int>>>() {
        override fun areItemsTheSame(
            oldItem: Pair<Pair<Int, Int>, Pair<Int, Int>>,
            newItem: Pair<Pair<Int, Int>, Pair<Int, Int>>,
        ) =
            oldItem == newItem

        override fun areContentsTheSame(
            oldItem: Pair<Pair<Int, Int>, Pair<Int, Int>>,
            newItem: Pair<Pair<Int, Int>, Pair<Int, Int>>,
        ) =
            oldItem.first == newItem.first && oldItem.second == newItem.second
    }
}