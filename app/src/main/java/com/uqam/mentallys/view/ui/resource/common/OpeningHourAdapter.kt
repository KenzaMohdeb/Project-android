package com.uqam.mentallys.view.ui.resource.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.uqam.mentallys.databinding.ItemOpeningHourBinding
import com.uqam.mentallys.model.Days

class OpeningHourAdapter :
    ListAdapter<Pair<Days, List<Pair<Pair<Int, Int>, Pair<Int, Int>>>>, OpeningHourAdapter.ViewHolder>(
        DiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemOpeningHourBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    class ViewHolder(
        private val binding: ItemOpeningHourBinding,
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Pair<Days, List<Pair<Pair<Int, Int>, Pair<Int, Int>>>>) {
            binding.apply {
                itemOpeningHourDay.setText(item.first.getTextId())
                val hourAdapter = HourAdapter()
                itemOpeningHourRecycler.apply {
                    val layoutManager = object : LinearLayoutManager(context) {
                        override fun canScrollHorizontally(): Boolean {
                            return false
                        }
                    }
                    (layoutManager as LinearLayoutManager).orientation =
                        LinearLayoutManager.VERTICAL
                    this.layoutManager = layoutManager
                    adapter = hourAdapter
                    setHasFixedSize(true)
                    itemAnimator = null
                }
                hourAdapter.submitList(item.second)
            }
        }
    }

    class DiffCallback :
        DiffUtil.ItemCallback<Pair<Days, List<Pair<Pair<Int, Int>, Pair<Int, Int>>>>>() {
        override fun areItemsTheSame(
            oldItem: Pair<Days, List<Pair<Pair<Int, Int>, Pair<Int, Int>>>>,
            newItem: Pair<Days, List<Pair<Pair<Int, Int>, Pair<Int, Int>>>>,
        ) =
            oldItem == newItem

        override fun areContentsTheSame(
            oldItem: Pair<Days, List<Pair<Pair<Int, Int>, Pair<Int, Int>>>>,
            newItem: Pair<Days, List<Pair<Pair<Int, Int>, Pair<Int, Int>>>>,
        ) =
            oldItem.first == newItem.first && oldItem.second == newItem.second
    }

}