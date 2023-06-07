package com.uqam.mentallys.view.ui.pathway.selecteventtype

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.uqam.mentallys.databinding.ItemEventTypeBinding
import com.uqam.mentallys.model.EventType

class SelectEventTypeAdapter(private val listener: OnItemClickListener) : ListAdapter<EventType, SelectEventTypeAdapter.EventTypesViewHolder>(
    DiffCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventTypesViewHolder {
        val binding =
            ItemEventTypeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventTypesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventTypesViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    inner class EventTypesViewHolder(val binding: ItemEventTypeBinding): RecyclerView.ViewHolder(binding.root) {

        init {
            binding.apply {
                root.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val eventType = getItem(position)
                        listener.onItemClick(eventType)
                    }
                }
            }
        }

        fun bind(eventType: EventType) {
            binding.apply {
                eventTypeName.text = eventType.typeName
                eventTypeImage.setImageResource(eventType.icon)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(eventType: EventType)
    }

    class DiffCallback() : DiffUtil.ItemCallback<EventType>() {
        override fun areItemsTheSame(oldItem: EventType, newItem: EventType) =
            oldItem.typeId == newItem.typeId

        override fun areContentsTheSame(oldItem: EventType, newItem: EventType) =
            oldItem == newItem
    }

}