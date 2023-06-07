package com.uqam.mentallys.view.ui.homepage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.uqam.mentallys.databinding.ItemMedicBinding

import com.uqam.mentallys.model.Medic

class MedicAdapter : ListAdapter<Medic, MedicAdapter.MedicViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedicViewHolder {
        val binding = ItemMedicBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MedicViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MedicViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    class MedicViewHolder(private val binding: ItemMedicBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(medic: Medic) {
            binding.apply {
                medicTitle.text = medic.title
                medicIcon.setImageResource(medic.icon)

            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Medic>() {
        override fun areItemsTheSame(oldItem: Medic, newItem: Medic) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Medic, newItem: Medic) =
            oldItem == newItem
    }

}