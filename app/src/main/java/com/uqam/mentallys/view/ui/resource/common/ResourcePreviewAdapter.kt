package com.uqam.mentallys.view.ui.resource.common

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.uqam.mentallys.R
import com.uqam.mentallys.databinding.ItemResourcePreviewBinding
import com.uqam.mentallys.model.resource.Resource
import com.uqam.mentallys.model.resource.Category

class ResourcePreviewAdapter :
    ListAdapter<Resource, ResourcePreviewAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemResourcePreviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, parent.context, parent.findNavController())
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    class ViewHolder(
        private val binding: ItemResourcePreviewBinding,
        private val context: Context,
        private val navController: NavController
    ) :
        RecyclerView.ViewHolder(binding.root) {

        private val cardAbleHybridAdapter = GenericOutlinedAdapter(false)

        fun bind(item: Resource) {
            binding.apply {
                itemResourcePreviewContainer.setOnClickListener{
                    val bundle = Bundle()
                    bundle.putSerializable("resourceInstance", item)
                    navController.navigate(R.id.resourceFragment,bundle)
                }
                itemResourceCategoryIcon.setImageResource(item.category.getImageId())
                itemResourceCategoryText.setText(item.category.getTextId())
                itemResourcePreviewTitle.text = item.name
                itemResourcePreviewClient.text =
                    item.clients.joinToString(" · ") { context.getString(it.getTextId()) }
                if (item.category == Category.PRIVATE) {
                    itemResourcePreviewType.text =
                        item.activities.joinToString(" · ") { context.getString(it.getTextId()) }
                } else {
                    itemResourcePreviewType.visibility = View.GONE
                }
                itemResourcePreviewTagRecycler.apply {
                    layoutManager = object : LinearLayoutManager(context) {
                        override fun canScrollHorizontally(): Boolean { return false }
                    }
                    (layoutManager as LinearLayoutManager).orientation =
                        LinearLayoutManager.HORIZONTAL
                    adapter = cardAbleHybridAdapter
                    setHasFixedSize(true)
                    itemAnimator = null
                }
                cardAbleHybridAdapter.submitList(listOf(item.cost) + item.modalities)
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