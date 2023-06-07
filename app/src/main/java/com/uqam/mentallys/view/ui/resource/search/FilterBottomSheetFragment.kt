package com.uqam.mentallys.view.ui.resource.search

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.res.Resources.getSystem
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.uqam.mentallys.R
import com.uqam.mentallys.databinding.FragmentBottomSheetFilterBinding
import com.uqam.mentallys.model.resource.*
import com.uqam.mentallys.view.ui.resource.common.GenericCardAdapter
import com.uqam.mentallys.view.ui.resource.common.GenericSelectableAdapter
import com.uqam.mentallys.view.ui.resource.common.ModalitiesAdapter
import com.uqam.mentallys.view.ui.resource.common.StringCardAdapter
import com.uqam.mentallys.viewmodels.resource.FilterViewModel
import com.uqam.mentallys.viewmodels.resource.ResourceViewModel


class FilterBottomSheetFragment :
    BottomSheetDialogFragment() {

    private val costsAdapter = GenericSelectableAdapter()
    private val clientsAdapter = GenericSelectableAdapter()
    private val tagsAdapter = StringCardAdapter(true)
    private val activitiesAdapter = GenericCardAdapter(true)
    private val modalitiesAdapter = ModalitiesAdapter()
    private val languagesAdapter = GenericSelectableAdapter()
    private val resourceViewModel: ResourceViewModel by viewModels({ requireParentFragment() })
    private val filtersViewModel: FilterViewModel by viewModels({ requireParentFragment() })
    var validateFilterCallback : ()->Unit = {}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_bottom_sheet_filter, container, false)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val temp = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        temp.setCanceledOnTouchOutside(true)
        temp.setOnShowListener { dialog ->
            val bottomDialog = dialog as BottomSheetDialog
            val bottomSheet =
                (bottomDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout?)!!
            val displayMetrics = requireActivity().resources.displayMetrics
            val height = displayMetrics.heightPixels
            BottomSheetBehavior.from(bottomSheet).peekHeight = height
        }
        return temp
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val oldFilter: FilterViewModel.ResourceFilter = filtersViewModel.filter.value!!.clone()
        val binding = FragmentBottomSheetFilterBinding.bind(view)
        binding.apply {
            bottomSheetFilter.setOnClickListener{
                dismiss()
            }
            // Transmit scroll event to the nested view but not to other clickable elements
            bottomSheetFilterHead.setOnTouchListener { _, p1 ->
                bottomSheetFilterScrollContainer.onTouchEvent(p1)
                true
            }
            bottomSheetFilterFooter.setOnTouchListener { _, p1 ->
                bottomSheetFilterScrollContainer.onTouchEvent(p1)
                true
            }
            costsAdapter.selectedItem.value = oldFilter.selectedCosts
            clientsAdapter.selectedItem.value = oldFilter.selectedClients
            tagsAdapter.selectedItem.value = oldFilter.selectedTags
            activitiesAdapter.selectedItem.value = oldFilter.selectedActivities
            modalitiesAdapter.selectedItem.value = oldFilter.selectedModalities
            languagesAdapter.selectedItem.value = oldFilter.selectedLanguages
            bottomSheetFilterCostRecycler.apply {
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                adapter = costsAdapter
                costsAdapter.submitList((Cost.values()).toList())
            }
            bottomSheetFilterClientRecycler.apply {
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                adapter = clientsAdapter
                clientsAdapter.submitList(Client.values().toList())
            }
            bottomSheetFilterTagsRecycler.apply {
                val layoutManager = object : FlexboxLayoutManager(context) {
                    override fun canScrollHorizontally(): Boolean {
                        return false
                    }
                }
                layoutManager.flexDirection = FlexDirection.ROW
                layoutManager.justifyContent = JustifyContent.FLEX_START
                this.layoutManager = layoutManager
                adapter = tagsAdapter
                setHasFixedSize(true)
                itemAnimator = null
                tagsAdapter.submitList(resourceViewModel.tagList.value)
            }
            bottomSheetFilterActivitiesRecycler.apply {
                val layoutManager = FlexboxLayoutManager(context)
                layoutManager.flexDirection = FlexDirection.ROW
                layoutManager.justifyContent = JustifyContent.FLEX_START
                this.layoutManager = layoutManager
                adapter = activitiesAdapter
                setHasFixedSize(true)
                itemAnimator = null
                activitiesAdapter.submitList(Activity.values().toList())
            }
            bottomSheetFilterModalitiesRecycler.apply {
                val layoutManager = FlexboxLayoutManager(context)
                layoutManager.flexDirection = FlexDirection.ROW
                layoutManager.alignItems = AlignItems.STRETCH
                layoutManager.justifyContent = JustifyContent.FLEX_START
                layoutManager.maxLine = 1
                this.layoutManager = layoutManager
                adapter = modalitiesAdapter
                setHasFixedSize(true)
                itemAnimator = null
                modalitiesAdapter.submitList(Modality.values().toList())
            }
            bottomSheetFilterLanguageRecycler.apply {
                layoutManager = object :
                    LinearLayoutManager(requireContext(), VERTICAL, false) {
                    override fun canScrollHorizontally(): Boolean {
                        return false
                    }
                }
                adapter = languagesAdapter
                languagesAdapter.submitList(Language.values().toList())
            }

            // Setup "display more" tags button
            val tagsRecyclerMaxHeight: Int = (222 * getSystem().displayMetrics.density).toInt()
            var tagsRecyclerDeployed = false
            bottomSheetFilterTagsMore.setOnClickListener {
                val constraintSet = ConstraintSet()
                constraintSet.clone(bottomSheetFilterScrollContainerLayout)
                if (tagsRecyclerDeployed) {
                    constraintSet.constrainMaxHeight(bottomSheetFilterTagsRecycler.id,
                        tagsRecyclerMaxHeight)
                    tagsRecyclerDeployed = false
                    bottomSheetFilterTagsMoreText.setText(R.string.bottom_sheet_filter_display_more)
                    bottomSheetFilterTagsMoreIcon.setImageDrawable(requireContext().getDrawable(R.drawable.ic_bottom_arrow))
                } else {
                    constraintSet.constrainMaxHeight(bottomSheetFilterTagsRecycler.id, -1)
                    tagsRecyclerDeployed = true
                    bottomSheetFilterTagsMoreText.setText(R.string.bottom_sheet_filter_display_less)
                    bottomSheetFilterTagsMoreIcon.setImageDrawable(requireContext().getDrawable(R.drawable.ic_top_arrow))
                }
                constraintSet.applyTo(bottomSheetFilterScrollContainerLayout)
            }

            // Setup "language more" tags button
            val languageRecyclerMaxHeight: Int = (117 * getSystem().displayMetrics.density).toInt()
            var languageRecyclerDeployed = false
            bottomSheetFilterLanguageMore.setOnClickListener {
                val constraintSet = ConstraintSet()
                constraintSet.clone(bottomSheetFilterScrollContainerLayout)
                if (languageRecyclerDeployed) {
                    constraintSet.constrainMaxHeight(bottomSheetFilterLanguageRecycler.id,
                        languageRecyclerMaxHeight)
                    languageRecyclerDeployed = false
                    bottomSheetFilterLanguageMoreText.setText(R.string.bottom_sheet_filter_display_more)
                    bottomSheetFilterLanguageMoreIcon.setImageDrawable(requireContext().getDrawable(
                        R.drawable.ic_bottom_arrow))
                } else {
                    constraintSet.constrainMaxHeight(bottomSheetFilterLanguageRecycler.id, -1)
                    languageRecyclerDeployed = true
                    bottomSheetFilterLanguageMoreText.setText(R.string.bottom_sheet_filter_display_less)
                    bottomSheetFilterLanguageMoreIcon.setImageDrawable(requireContext().getDrawable(
                        R.drawable.ic_top_arrow))
                }
                constraintSet.applyTo(bottomSheetFilterScrollContainerLayout)
            }
            // Close button setup
            bottomSheetFilterClose.setOnClickListener {
                filtersViewModel.filter.value = oldFilter
                dismiss()
            }

            resourceViewModel.list.observe(viewLifecycleOwner) {
                bottomSheetFilterDisplayResult.text =
                    requireContext().resources.getQuantityString(R.plurals.bottom_sheet_filter_display_services,
                        it.size,
                        it.size)
            }
            bottomSheetFilterDisplayResult.text =
                requireContext().resources.getQuantityString(R.plurals.bottom_sheet_filter_display_services,
                    resourceViewModel.list.value?.size!!,
                    resourceViewModel.list.value?.size!!)
            bottomSheetFilterDisplayResult.setOnClickListener {
                dismiss()
                validateFilterCallback()
            }
            bottomSheetFilterReset.setOnClickListener {
                costsAdapter.selectedItem.value = listOf()
                clientsAdapter.selectedItem.value = listOf()
                tagsAdapter.selectedItem.value = listOf()
                activitiesAdapter.selectedItem.value = listOf()
                modalitiesAdapter.selectedItem.value = listOf()
                languagesAdapter.selectedItem.value = listOf()
            }
            costsAdapter.selectedItem.observe(viewLifecycleOwner) {
                val filter = filtersViewModel.filter.value
                filter?.costs = it.filterIsInstance(Cost::class.java)
                filtersViewModel.filter.value = filter
            }
            clientsAdapter.selectedItem.observe(viewLifecycleOwner) {
                val filter = filtersViewModel.filter.value
                filter?.clients = it.filterIsInstance(Client::class.java)
                filtersViewModel.filter.value = filter
            }
            tagsAdapter.selectedItem.observe(viewLifecycleOwner) {
                val filter = filtersViewModel.filter.value
                filter?.tags = it
                filtersViewModel.filter.value = filter
            }
            activitiesAdapter.selectedItem.observe(viewLifecycleOwner) {
                val filter = filtersViewModel.filter.value
                filter?.activities = it.filterIsInstance(Activity::class.java)
                filtersViewModel.filter.value = filter
            }
            modalitiesAdapter.selectedItem.observe(viewLifecycleOwner) {
                val filter = filtersViewModel.filter.value
                @Suppress("UselessCallOnCollection")
                filter?.modalities = it.filterIsInstance(Modality::class.java)
                filtersViewModel.filter.value = filter
            }
            languagesAdapter.selectedItem.observe(viewLifecycleOwner) {
                val filter = filtersViewModel.filter.value
                filter?.languages = it.filterIsInstance(Language::class.java)
                filtersViewModel.filter.value = filter
            }
        }
        super.onViewCreated(view, savedInstanceState)
    }


    override fun dismiss() {
        super.dismiss()
        onDestroy()
    }
}