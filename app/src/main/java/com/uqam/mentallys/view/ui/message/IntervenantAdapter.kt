package com.uqam.mentallys.view.ui.message

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.uqam.mentallys.databinding.ChatUserInfoBinding
import com.uqam.mentallys.model.ChatResourceLoc

class IntervenantAdapter(private val listener: OnItemClickListener,
                         private val buttonClicked:OnButtonClickListener) :
    ListAdapter<ChatResourceLoc, IntervenantAdapter.IntervenantViewModel>(
    DiffCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IntervenantViewModel {
        val binding =
            ChatUserInfoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return IntervenantViewModel(binding)
    }

    override fun onBindViewHolder(holder: IntervenantViewModel, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    inner class IntervenantViewModel(val binding: ChatUserInfoBinding):
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
                buttonClicked.onButtonClick(itemFavorite)
            }
        }

        fun bind(resource: ChatResourceLoc) {
            binding.apply {
                resourceId.text= resource.id.toString()
              /*  Picasso.get()
                    .load(resource.icon!!)
                    .resize(14, 14)
                    .centerCrop()
                    .into(itemSectorIcon)*/
               // itemSectorName.text = toDo
                intervenantName.text = resource.name
                itemProfessionalName.text = resource.activities.joinToString()
                itemService.text = resource.tag.joinToString (" . ")
                Picasso.get()
                    .load(resource.icon)
                    .resize(55, 55)
                    .centerCrop()
                    .into(intervenantImg)

                itemKlm.text= resource.distance
                itemDisponibiliry.text = resource.disponible
               // eventTypeImage.setImageURI(Uri.parse(resource.profileImageUrl!!))
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(resource: ChatResourceLoc)
    }

    interface OnButtonClickListener {
        fun onButtonClick(view: View)
    }

    class DiffCallback() : DiffUtil.ItemCallback<ChatResourceLoc>() {
        override fun areItemsTheSame(oldResource: ChatResourceLoc, newResource: ChatResourceLoc) =
            oldResource.id == newResource.id

        override fun areContentsTheSame(oldResource: ChatResourceLoc, newResource: ChatResourceLoc) =
            oldResource == newResource
    }

}