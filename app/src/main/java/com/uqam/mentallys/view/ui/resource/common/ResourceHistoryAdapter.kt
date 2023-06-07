package com.uqam.mentallys.view.ui.resource.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.uqam.mentallys.R
import com.uqam.mentallys.databinding.ItemResourceHistoryBinding
import com.uqam.mentallys.model.resource.Resource

class ResourceHistoryAdapter (private val keepFocusOnCallBack : () -> Unit) :
    ListAdapter<Resource, ResourceHistoryAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemResourceHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, parent.findNavController(), keepFocusOnCallBack)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    class ViewHolder(
        private val binding: ItemResourceHistoryBinding,
        private val navController: NavController,
        private val keepFocusOnCallBack : () -> Unit,
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Resource) {
            binding.apply {
                itemResourceHistoryName.text = item.name
                itemResourceHistoryCategoryIcon.setImageResource(item.category.getImageId())
                itemResourceHistoryCategoryText.setText(item.category.getTextId())
                itemResourceHistoryContainer.setOnClickListener {
                    keepFocusOnCallBack()
                    val bundle = Bundle()
                    bundle.putSerializable("resourceInstance", item)
                    navController.navigate(R.id.resourceFragment, bundle)
                }
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Resource>() {
        override fun areItemsTheSame(oldItem: Resource, newItem: Resource) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Resource, newItem: Resource) =
            oldItem.name == newItem.name
    }
}
