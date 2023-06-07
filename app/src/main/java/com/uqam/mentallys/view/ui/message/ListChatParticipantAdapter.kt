package com.uqam.mentallys.view.ui.message

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.uqam.mentallys.databinding.ChatParticipantBinding
import com.uqam.mentallys.model.ChatParticipantInfo
import com.uqam.mentallys.utils.DateFormatter
import okhttp3.internal.UTC
import java.text.SimpleDateFormat

class ListChatParticipantAdapter(private val listener: OnItemClickListener) :
    ListAdapter<ChatParticipantInfo, ListChatParticipantAdapter.ParticipantViewModel>(
    DiffCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParticipantViewModel {
        val binding =
            ChatParticipantBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ParticipantViewModel(binding)
    }

    override fun onBindViewHolder(holder: ParticipantViewModel, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    inner class ParticipantViewModel(val binding: ChatParticipantBinding):
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.apply {
                root.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val resource = getItem(position)
                        listener.onItemClick(resource)
                    }

                }
            }
        }

        fun bind(resource: ChatParticipantInfo) {
            binding.apply {
                threadId.text = resource.threadId
                CommunicationUserId.text = resource.communicationId
                participantName.text = resource.displayName
                connectedTime.text = DateFormatter.format(resource.shareHistoryTime, "HH:mm")
                lastMessage.text = resource.lastMessage
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(resource: ChatParticipantInfo)
    }

    class DiffCallback() : DiffUtil.ItemCallback<ChatParticipantInfo>() {
        override fun areItemsTheSame(oldResource: ChatParticipantInfo, newResource: ChatParticipantInfo) =
            oldResource.threadId == newResource.threadId

        override fun areContentsTheSame(oldResource: ChatParticipantInfo, newResource: ChatParticipantInfo) =
            oldResource == newResource
    }

}