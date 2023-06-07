package com.uqam.mentallys.view.ui.resource

import android.content.Intent
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.uqam.mentallys.R
import com.uqam.mentallys.databinding.FragmentResourceBinding
import com.uqam.mentallys.model.resource.Resource
import com.uqam.mentallys.utils.SharedState
import com.uqam.mentallys.view.ui.resource.common.GenericCardAdapter
import com.uqam.mentallys.view.ui.resource.common.GenericOutlinedAdapter
import com.uqam.mentallys.view.ui.resource.common.OpeningHourAdapter
import com.uqam.mentallys.view.ui.resource.common.StringCardAdapter
import com.uqam.mentallys.viewmodels.resource.ResourceViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ResourceFragment : Fragment(R.layout.fragment_resource) {

    private lateinit var resource: Resource
    private val resourceViewModel : ResourceViewModel by viewModels()
    private val sharedState: SharedState = SharedState
    private val cardAbleHybridAdapter = GenericOutlinedAdapter(false)
    private val clientsGenericCardAdapter = GenericCardAdapter()
    private val tagsStringCardAdapter = StringCardAdapter()
    private val activitiesGenericCardAdapter = GenericCardAdapter()
    private val openingHourAdapter = OpeningHourAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.white)
        // Retrieve the resource from the bundle
        if (arguments != null) {
            resource = requireArguments().getSerializable("resourceInstance") as Resource
            sharedState.saveState("ResourcePackageOpenedResource", resource)
            resourceViewModel.saveInHistory(resource)
        }
        // Apply the resource attribute to the layout
        val binding = FragmentResourceBinding.bind(view)
        setupToolbar(binding)
        setupHeader(binding, resource)
        setupBody(binding, resource)
        setupFooter(binding, resource)
        setupModificationButton(binding, resource)
    }

    private fun setupToolbar(binding: FragmentResourceBinding) {
        binding.apply {
            // Set up the toolbar
            fragmentResourceToolbar.inflateMenu(R.menu.resource_menu)
            fragmentResourceToolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
            fragmentResourceToolbar.navigationIcon?.setTint(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.slate
                )
            )
            fragmentResourceToolbar.setNavigationOnClickListener {
                parentFragmentManager.popBackStack()
            }
        }
    }

    private fun setupHeader(binding: FragmentResourceBinding, resource: Resource) {
        binding.apply {
            // Set the header image from a URL and display placeholder and error image if it fails
            val unwrappedErrorDrawable: Drawable = when (resource.category.getImageId()) {
                R.drawable.ic_baseline_hospital_24 -> AppCompatResources.getDrawable(requireContext(), R.drawable.ic_hospital_placeholder)!!
                R.drawable.ic_baseline_people_24 -> AppCompatResources.getDrawable(requireContext(), R.drawable.ic_community_placeholder)!!
                R.drawable.ic_baseline_couch_24 -> AppCompatResources.getDrawable(requireContext(), R.drawable.ic_private_placeholder)!!
                else -> AppCompatResources.getDrawable(requireContext(), R.drawable.ic_community_placeholder)!!
            }
            val wrappedErrorDrawable = DrawableCompat.wrap(unwrappedErrorDrawable)
            //DrawableCompat.setTint(wrappedErrorDrawable, ContextCompat.getColor(context!!, R.color.colorPrimary))
            DrawableCompat.setHotspotBounds(wrappedErrorDrawable, 20, 20, 20, 20)
            Glide.with(this@ResourceFragment).asBitmap()
                .load(resource.image)
                .placeholder(wrappedErrorDrawable)
                .error(wrappedErrorDrawable)
                .override((82 * Resources.getSystem().displayMetrics.density).toInt())
                .centerCrop()
                .into(fragmentResourceImage)
            // Set up the category card
            fragmentResourceCategoryIcon.setImageResource(resource.category.getImageId())
            fragmentResourceCategoryText.setText(resource.category.getTextId())
            // Set up the top section of the resource page
            fragmentResourceTitle.text = resource.name

            // Compose the address from the different resource attributes
            fragmentResourceAddress.text =
                "${resource.address}, ${resource.city}, ${resource.region}, ${resource.postalCode}, ${resource.country}"
            // Display the description if not null or empty
            if (resource.description == "" || resource.description == null) {
                fragmentResourceDescriptionCard.visibility = View.GONE
            } else {
                fragmentResourceDescriptionCardText.text = resource.description
            }
            // Bind the head recycler to the corresponding adapter
            itemResourcePreviewTagRecycler.apply {
                val layoutManager = object : LinearLayoutManager(context) {
                    override fun canScrollHorizontally(): Boolean {
                        return false
                    }
                }
                (layoutManager as LinearLayoutManager).orientation =
                    LinearLayoutManager.HORIZONTAL
                this.layoutManager = layoutManager
                adapter = cardAbleHybridAdapter
                setHasFixedSize(true)
                itemAnimator = null
            }
            cardAbleHybridAdapter.submitList(listOf(resource.cost) + resource.modalities)
            setupMailButton(binding, resource)
            setupCallButton(binding, resource)
            setupNavigationButton(binding, resource)
        }
    }

    /*
    Set up a call url and bind it to a button
     */
    private fun setupCallButton(binding: FragmentResourceBinding, resource: Resource) {
        binding.apply {
            if (resource.phone == "" || resource.phone == null) {
                fragmentResourceCall.visibility = View.GONE
            } else {
                fragmentResourceCall.setOnClickListener {
                    val callIntent = Intent(Intent.ACTION_DIAL)
                    callIntent.data = Uri.parse("tel:${resource.phone}")
                    if (callIntent.resolveActivity(context?.packageManager!!) != null) {
                        startActivity(callIntent)
                    }
                }
            }
        }
    }

    /*
     Set up a mailing url and bind it to a button
    */
    private fun setupMailButton(binding: FragmentResourceBinding, resource: Resource) {
        binding.apply {
            if (resource.mail == "" || resource.mail == null) {
                fragmentResourceMessage.visibility = View.GONE
            } else {
                fragmentResourceMessage.setOnClickListener {
                    val mailIntent = Intent(Intent.ACTION_SENDTO)
                    mailIntent.data = Uri.parse("mailto:${resource.mail}")
                    if (mailIntent.resolveActivity(context?.packageManager!!) != null) {
                        startActivity(mailIntent)
                    }
                }
                if (resource.phone == "" || resource.phone == null) {
                    fragmentResourceMessage.setBackgroundColor(ContextCompat.getColor(requireContext(),
                        R.color.colorPrimary))
                    fragmentResourceMessage.setTextColor(ContextCompat.getColor(requireContext(),
                        R.color.white))
                    fragmentResourceMessage.iconTint =
                        ContextCompat.getColorStateList(requireContext(), R.color.white)
                }

            }
        }
    }

    /*
     Set up a geo link to the resource and bind it to a button
     */
    private fun setupNavigationButton(binding: FragmentResourceBinding, resource: Resource) {
        binding.apply {
            fragmentResourceNavigate.setOnClickListener {
                val uri =
                    "geo:?q=${resource.name}+${resource.address}+${resource.city}+${resource.postalCode} (${resource.name})"
                val navigateIntent = Intent(Intent.ACTION_VIEW)
                navigateIntent.data = Uri.parse(uri)
                if (navigateIntent.resolveActivity(context?.packageManager!!) != null) {
                    startActivity(navigateIntent)
                }
            }
        }
    }


    private fun setupBody(binding: FragmentResourceBinding, resource: Resource) {
        binding.apply {
            // Set up all the recycler and bind the correct data to it
            fragmentResourceClientsRecycler.apply {
                val layoutManager = object : FlexboxLayoutManager(context) {
                    override fun canScrollHorizontally(): Boolean {
                        return false
                    }

                    override fun canScrollVertically(): Boolean {
                        return false
                    }
                }
                layoutManager.flexDirection = FlexDirection.ROW
                layoutManager.justifyContent = JustifyContent.FLEX_START
                this.layoutManager = layoutManager
                adapter = clientsGenericCardAdapter
                setHasFixedSize(true)
                itemAnimator = null
            }
            clientsGenericCardAdapter.submitList(resource.clients)
            fragmentResourceTagsRecycler.apply {
                val layoutManager = object : FlexboxLayoutManager(context) {
                    override fun canScrollHorizontally(): Boolean {
                        return false
                    }

                    override fun canScrollVertically(): Boolean {
                        return false
                    }
                }
                layoutManager.flexDirection = FlexDirection.ROW
                layoutManager.justifyContent = JustifyContent.FLEX_START
                this.layoutManager = layoutManager
                adapter = tagsStringCardAdapter
                setHasFixedSize(true)
                itemAnimator = null
            }
            tagsStringCardAdapter.submitList(resource.tags)
            fragmentResourceActivityRecycler.apply {
                val layoutManager = object : FlexboxLayoutManager(context) {
                    override fun canScrollHorizontally(): Boolean {
                        return false
                    }

                    override fun canScrollVertically(): Boolean {
                        return false
                    }
                }
                layoutManager.flexDirection = FlexDirection.ROW
                layoutManager.justifyContent = JustifyContent.FLEX_START
                this.layoutManager = layoutManager
                adapter = activitiesGenericCardAdapter
                setHasFixedSize(true)
                itemAnimator = null
            }
        }
    }

    // Set up the last section of the layout
    private fun setupFooter(binding: FragmentResourceBinding, resource: Resource) {
        binding.apply {
            activitiesGenericCardAdapter.submitList(resource.activities)
            if (resource.openingHour == null || resource.openingHour.isEmpty()) {
                fragmentResourceOtherOpeningHoursRecycler.visibility = View.GONE
                fragmentResourceOtherOpeningHoursIcon.visibility = View.GONE
                fragmentResourceOtherSeparator.visibility = View.GONE
            } else {
                fragmentResourceOtherOpeningHoursRecycler.apply {
                    layoutManager =
                        LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                    adapter = openingHourAdapter
                    setHasFixedSize(true)
                }
                openingHourAdapter.submitList(resource.openingHour)
            }
            fragmentResourceOtherLanguageText.text = resource.languages.joinToString(" · ") { requireContext().getString(it.getTextId()) }
        }
    }

    private fun setupModificationButton(binding : FragmentResourceBinding, resource: Resource){
        binding.apply {
                fragmentResourceModificationButton.setOnClickListener {
                    val mailIntent = Intent(Intent.ACTION_SENDTO)
                    mailIntent.data = Uri.parse("mailto:admin@mentallys.com")
                    mailIntent.putExtra(Intent.EXTRA_SUBJECT,
                        "${context?.getText(R.string.email_modification_subject).toString()} ${resource.title}")
                    mailIntent.putExtra(Intent.EXTRA_TEXT, context?.getText(R.string.email_modification_template))
                    if (mailIntent.resolveActivity(context?.packageManager!!) != null) {
                        startActivity(mailIntent)
                    }
                }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        sharedState.eraseState("ResourcePackageOpenedResource")
    }

}