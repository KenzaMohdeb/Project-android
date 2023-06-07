package com.uqam.mentallys.view.ui.message

import android.accounts.AccountManager.get
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatDrawableManager.get
import androidx.appcompat.widget.ResourceManagerInternal.get
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.squareup.picasso.NetworkPolicy
import com.uqam.mentallys.data.datasources.ChatDataSourceLocal
import com.uqam.mentallys.databinding.FragmentIntervenantProfileBinding
import com.uqam.mentallys.model.ChatResource
import com.uqam.mentallys.model.ChatResourceLoc
import java.util.*

class IntervenantProfileAdapter(private val listener: OnItemClickListener) : ListAdapter<ChatResourceLoc, IntervenantProfileAdapter.IntervenantViewModel>(
    DiffCallback()
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IntervenantViewModel {
        val binding =
            FragmentIntervenantProfileBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return IntervenantViewModel(binding)
    }
    override fun onBindViewHolder(holder: IntervenantViewModel, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }
    inner class IntervenantViewModel(val binding: FragmentIntervenantProfileBinding): RecyclerView.ViewHolder(binding.root) {

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

        fun bind(resource: ChatResourceLoc) {
            binding.apply {
                //na.text= resource.id.toString()
              /*  Picasso.get()
                    .load(resource.icon!!)
                    .resize(14, 14)
                    .centerCrop()
                    .into(itemSectorIcon)*/
               // itemSectorName.text = toDo
                intervenantName.text = resource.name
               // itemProfessional.text = resource.activities.joinToString()
                intervenantSexe.text = resource.sexe
                resourceKlm.text = resource.distance;
                resourceDisponibiliry.text =  resource.disponible
                Picasso.get()
                    .load(resource.icon)
                    .resize(55, 55)
                    .centerCrop()
                    .into(intervenantImg)
                intervenantDescription.text = resource.description
                //itemKlm.text= resource.distance
                //itemDisponibiliry.text = resource.disponible
               // eventTypeImage.setImageURI(Uri.parse(resource.profileImageUrl!!))
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(resource: ChatResourceLoc)
    }

    class DiffCallback() : DiffUtil.ItemCallback<ChatResourceLoc>() {
        override fun areItemsTheSame(oldResource: ChatResourceLoc, newResource: ChatResourceLoc) =
            oldResource.id == newResource.id

        override fun areContentsTheSame(oldResource: ChatResourceLoc, newResource: ChatResourceLoc) =
            oldResource == newResource
    }

}