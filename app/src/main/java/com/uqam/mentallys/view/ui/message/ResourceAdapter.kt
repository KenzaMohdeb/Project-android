package com.uqam.mentallys.view.ui.message

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.uqam.mentallys.databinding.ChatUserInfoBinding
import com.uqam.mentallys.model.ChatResource

class ResourceAdapter(private val listener: OnItemClickListener) : ListAdapter<ChatResource, ResourceAdapter.ResourceViewHolder>(
    DiffCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResourceViewHolder {
        val binding =
            ChatUserInfoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ResourceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ResourceViewHolder, position: Int) {
        getItem(position)
        holder.bind()
    }

    inner class ResourceViewHolder(val binding: ChatUserInfoBinding): RecyclerView.ViewHolder(binding.root)
    {
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

        fun bind() {
            binding.apply {
               /* eventTypeName.text = resource.fullName
                Picasso.get()
                    .load(resource.profileImageUrl!!)
                    .resize(300, 300)
                    .centerCrop()
                    .into(eventTypeImage)
               // eventTypeImage.setImageURI(Uri.parse(resource.profileImageUrl!!))*/
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(resource: ChatResource)
    }

    class DiffCallback() : DiffUtil.ItemCallback<ChatResource>() {
        override fun areItemsTheSame(oldResource: ChatResource, newResource: ChatResource) =
            oldResource.id == newResource.id

        override fun areContentsTheSame(oldResource: ChatResource, newResource: ChatResource) =
            oldResource == newResource
    }

}