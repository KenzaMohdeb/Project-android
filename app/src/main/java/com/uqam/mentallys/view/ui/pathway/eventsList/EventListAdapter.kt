package com.uqam.mentallys.view.ui.pathway.eventsList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.uqam.mentallys.R
import com.uqam.mentallys.databinding.ItemEventBinding
import com.uqam.mentallys.model.Event
import com.uqam.mentallys.model.EventTypeModels
import com.uqam.mentallys.utils.getDate
import java.util.concurrent.TimeUnit

class EventsAdapter(private val listener: OnItemClickListener) :
    ListAdapter<Event, EventsAdapter.EventsViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventsViewHolder {
        val binding = ItemEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventsViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
        holder.compareEvents(position)
    }

    inner class EventsViewHolder(private val binding: ItemEventBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.apply {
                root.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val event = getItem(position)
                        listener.onItemClick(event)
                    }
                }
            }
        }

        fun bind(event: Event) {

            binding.apply {

                eventName.text = event.name.typeName
//                eventDescription.text = event.name.icon.toString()
                eventImage.setBackgroundResource(event.name.icon)
                eventStartDate.text = getDate(event.startDate, "dd MMM yyyy")

                when (event.name.typeName) {
                    EventTypeModels().maintenance,
                    EventTypeModels().consultation,
                    EventTypeModels().psychotherapy -> {
                        eventDescription.text = event.jobTag
                    }
                    EventTypeModels().Hospitalization,
                    EventTypeModels().Accommodation -> {
                        eventDescription.text = event.place
                    }
                    EventTypeModels().CommunitySupport -> {
                        eventDescription.text = event.context
                    }
                    EventTypeModels().Other -> {
                        eventDescription.text = "Autre"
                    }
                }

                // Future Events
                if (event.startDate > System.currentTimeMillis()) {
                    val daysRemain =
                        TimeUnit.MILLISECONDS.toDays(event.startDate - System.currentTimeMillis())
                    if (daysRemain > 0) {
                        leftBorderCardView.setBackgroundResource(R.color.red)
                        futureEventDateRemainContainer.visibility = View.VISIBLE
                        futureEventDateRemain.text = "- $daysRemain jours"
                    }
                } else {
                    leftBorderCardView.setBackgroundResource(R.color.blue)
                    futureEventDateRemainContainer.visibility = View.GONE
                }
            }
        }

        // Calculate days between two events
        fun compareEvents(itemPosition: Int) {
            val currentEvent = getItem(itemPosition)
            val listCount = this@EventsAdapter.itemCount

            if (listCount > 1 && itemPosition < listCount - 1) {
                val prevEvent = getItem(itemPosition + 1)
                val dateDifference = currentEvent.startDate - prevEvent.startDate //+ 86400000
                var daysBetween = TimeUnit.MILLISECONDS.toDays(dateDifference)
                binding.apply {
                    compareDatesSection.visibility = View.VISIBLE

                    if (dateDifference.toString() == "0") {
                        eventRemainDate.text = "Le même jour"
                    } else {
                        eventRemainDate.text = "$daysBetween jours"
                    }
                }
            } else {
                binding.compareDatesSection.visibility = View.GONE
                return
            }

        }
    }

    interface OnItemClickListener {
        fun onItemClick(event: Event)
    }

    class DiffCallback : DiffUtil.ItemCallback<Event>() {
        override fun areItemsTheSame(oldItem: Event, newItem: Event) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Event, newItem: Event) =
            oldItem == newItem

    }

}

