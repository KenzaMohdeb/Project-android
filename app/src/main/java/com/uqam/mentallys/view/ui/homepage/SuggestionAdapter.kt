package com.uqam.mentallys.view.ui.homepage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.uqam.mentallys.databinding.ItemSuggestionBinding
import com.uqam.mentallys.model.Suggestion

class SuggestionAdapter :
    ListAdapter<Suggestion, SuggestionAdapter.SuggestionViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuggestionViewHolder {
        val binding =
            ItemSuggestionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SuggestionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SuggestionViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    class SuggestionViewHolder(private val binding: ItemSuggestionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(suggestion: Suggestion) {
            binding.apply {
                suggestionDescription.text = suggestion.description
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Suggestion>() {
        override fun areItemsTheSame(oldItem: Suggestion, newItem: Suggestion) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Suggestion, newItem: Suggestion) =
            oldItem == newItem
    }

}