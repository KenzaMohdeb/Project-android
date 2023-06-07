package com.uqam.mentallys.view.ui.message

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.uqam.mentallys.databinding.ChatParticipantBinding
import com.uqam.mentallys.model.ChatParticipantInfo
import com.uqam.mentallys.utils.DateFormatter


class ParticipantsChatListAdapter(
    val userList:ArrayList<ChatParticipantInfo>,
    private val listener: OnItemClickListener
):
    RecyclerView.Adapter<ParticipantsChatListAdapter.ParticipantViewModel>()
{
    inner class ParticipantViewModel(val binding: ChatParticipantBinding):
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.apply {
                root.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val resource = userList[position]
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
                connectedTime.text = DateFormatter.format(resource.shareHistoryTime, "d MMMM yyyy")
                lastMessage.text = "last message" //resource.lastMessage
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParticipantViewModel {
        val binding =
            ChatParticipantBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ParticipantViewModel(binding)
    }

    override fun onBindViewHolder(holder: ParticipantViewModel, position: Int) {
        val currentItem = userList[position]
        holder.bind(currentItem)
    }

    override fun getItemCount(): Int {
        return  userList.size
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