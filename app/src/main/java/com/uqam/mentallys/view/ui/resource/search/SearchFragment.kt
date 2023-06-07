package com.uqam.mentallys.view.ui.resource.search

import android.graphics.Rect
import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.uqam.mentallys.R
import com.uqam.mentallys.databinding.FragmentSearchBinding
import com.uqam.mentallys.model.resource.Category
import com.uqam.mentallys.model.resource.Resource
import com.uqam.mentallys.utils.SharedState
import com.uqam.mentallys.view.ui.resource.common.CategoriesAdapter
import com.uqam.mentallys.view.ui.resource.common.ResourceHistoryAdapter
import com.uqam.mentallys.view.ui.resource.list.ListFragment
import com.uqam.mentallys.view.ui.resource.map.MapFragment
import com.uqam.mentallys.viewmodels.resource.FilterViewModel
import com.uqam.mentallys.viewmodels.resource.ResourceViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search) {

    private var binding: FragmentSearchBinding? = null
    private val sharedState: SharedState = SharedState
    private val resourceViewModel: ResourceViewModel by viewModels()
    private val filterViewModel: FilterViewModel by viewModels()
    private val resourceCategoryAdapter = CategoriesAdapter()
    private lateinit var resourceHistoryAdapter: ResourceHistoryAdapter
    private var heightOfKeyboard: MutableLiveData<Int> = MutableLiveData(0)
    private var isLoadingEnabled: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (sharedState.getState("ResourcePackageOpenedResource") != null) {
            val bundle = Bundle()
            val item: Resource = sharedState.getState("ResourcePackageOpenedResource") as Resource
            bundle.putSerializable("resourceInstance", item)
            findNavController().navigate(R.id.resourceFragment, bundle)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        listenKeyboardHeight()
        setBackEventListener()
        // Setup the status bar color
        requireActivity().window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.white)
        val view = super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentSearchBinding.bind(view!!)
        restoreSharedState()
        saveState()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Get an instance of the input manager and save it as class property fo further uses
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            resourceHistoryAdapter = ResourceHistoryAdapter {
                searchInputCard.keepFocus()
                searchInputCard.ignoreStateChange = true
            }
            setupBottomSheetFilter()
            setupMacroFilter()
            setupHistory()
            setupAdvancedInputCard()
        }
    }

    private fun saveState() {
        binding?.apply {
            filterViewModel.filter.observe(viewLifecycleOwner) {
                sharedState.saveState("SearchFragmentFilter", filterViewModel.filter.value!!)
                resourceViewModel.fetchWithFilter(it)
                if (it.isThereActiveAdvancedFilter()) {
                    searchInputCard.setFilterActive()
                } else {
                    searchInputCard.setFilterInactive()
                }
            }
        }
    }

    private fun setupAdvancedInputCard() {
        binding?.apply {
            // Search bar listener setup
            searchInputCard.onTextUpdateCallBack = { text ->
                filterViewModel.filter.value?.searchString = text
            }
            searchInputCard.onEnterPressedCallback = { _, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN) {
                    val filter = filterViewModel.filter.value
                    filterViewModel.filter.value = filter
                    displayLoading()
                    resourceViewModel.list.observeOnce(viewLifecycleOwner) {
                        centerOnResult()
                        hideLoading()
                    }
                }
            }
            searchInputCard.onSearchActiveCallBack = {
                fragmentSearchHistoryContainer.visibility = View.VISIBLE
                sharedState.saveState("SearchFragmentIsHistoryOpened", true)
            }
            searchInputCard.onSearchInactiveCallBack = {
                fragmentSearchHistoryContainer.visibility = View.GONE
                sharedState.saveState("SearchFragmentIsHistoryOpened", false)
            }
            searchInputCard.onSearchBackPressedCallBack = {
                resourceViewModel.fetchWithFilter(filterViewModel.filter.value!!)
            }
        }
    }

    private fun setupMacroFilter() {
        binding?.apply {
            // Marco filter setup
            fragmentSearchCategoryRecycler.apply {
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                adapter = resourceCategoryAdapter
                setHasFixedSize(true)
            }
            resourceCategoryAdapter.submitList(Category.values().toList())
        }
        resourceCategoryAdapter.selectedCategory.observe(viewLifecycleOwner) {
            val filter = filterViewModel.filter.value
            filter?.categories = it
            filterViewModel.filter.value = filter
            displayLoading()
            resourceViewModel.list.observeOnce(viewLifecycleOwner) {
                centerOnResult()
                hideLoading()
            }
        }
    }

    private fun setupHistory() {
        resourceViewModel.getHistory()
        binding?.apply {
            heightOfKeyboard.observe(viewLifecycleOwner) {
                val params: ConstraintLayout.LayoutParams =
                    fragmentSearchHistoryRecyclerWrapper.layoutParams as ConstraintLayout.LayoutParams
                params.setMargins(0, 0, 0, heightOfKeyboard.value!!)
                fragmentSearchHistoryRecyclerWrapper.layoutParams = params
            }
            fragmentSearchHistoryRecycler.apply {
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                adapter = resourceHistoryAdapter
                setHasFixedSize(true)
            }
            resourceViewModel.history.observe(viewLifecycleOwner) {
                resourceHistoryAdapter.submitList(it)
                when (it.isEmpty()) {
                    true -> fragmentSearchHistoryTitle.visibility = View.GONE
                    false -> fragmentSearchHistoryTitle.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun setupBottomSheetFilter() {
        // Bottom sheet filter setup
        binding?.apply {
            searchInputCard.onFilterButtonClickCallBack = {
                val bottomSheetDialog = FilterBottomSheetFragment()
                if (childFragmentManager.findFragmentByTag("FilterBottomSheet") == null) {
                    bottomSheetDialog.show(childFragmentManager, "FilterBottomSheet")
                    bottomSheetDialog.isCancelable = true
                    bottomSheetDialog.validateFilterCallback = {
                        displayLoading()
                        resourceViewModel.list.observeOnce(viewLifecycleOwner) {
                            centerOnResult()
                            hideLoading()
                        }
                    }
                }
            }
        }
    }

    private fun centerOnResult() {
        binding?.apply {
            val mapFragment = fragmentSearchMapFragment.getFragment<MapFragment>()
            when (resourceViewModel.list.value?.size) {
                0 -> { /* Don't trigger else */
                }
                1 -> {
                    val temp =
                        mapFragment.getCurrentPointer()
                            ?.let { resourceViewModel.getNearestPoint(it) }
                    if (temp != null) {
                        mapFragment.centerOnResource(temp)
                    }
                }
                null -> { /* Avoid weird behavior */
                }
                else -> {mapFragment.applyBounds(resourceViewModel.getLatLngBounds())}
            }
        }

    }

    /*
     * Clear all filter when the back button is pressed
     */
    private fun setBackEventListener() {
        activity?.onBackPressedDispatcher?.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (filterViewModel.filter.value?.isThereAnyActiveFilter() == true) {
                    filterViewModel.filter.value?.reset()
                    binding?.searchInputCard?.setInputText("")
                    binding?.searchInputCard?.setStateEmpty()
                    resourceCategoryAdapter.selectedCategory.value = listOf()
                }else {
                    when (sharedState.getState("SearchFragmentSelectedSubFragment")) {
                        ListFragment::class -> this@SearchFragment.applyMapFragment()
                        else -> findNavController().popBackStack()
                    }
                }
            }
        })
    }

    private fun restoreSharedState() {
        if (sharedState.getState("SearchFragmentSelectedSubFragment") == null) {
            applyMapFragment()
        } else {
            when (sharedState.getState("SearchFragmentSelectedSubFragment")!!) {
                MapFragment::class -> applyMapFragment()
                ListFragment::class -> applyListFragment()
            }
        }
        if (sharedState.getState("SearchFragmentFilter") == null) {
            sharedState.saveState("SearchFragmentFilter", filterViewModel.filter.value!!)
        } else {
            filterViewModel.filter.value =
                sharedState.getState("SearchFragmentFilter") as FilterViewModel.ResourceFilter
            resourceCategoryAdapter.selectedCategory.value =
                filterViewModel.filter.value?.selectedCategories?.toList()
            binding?.searchInputCard?.setInputText(filterViewModel.filter.value?.searchString.toString())
        }
        if (sharedState.getState("SearchFragmentIsHistoryOpened") == true) {
            binding?.fragmentSearchHistoryContainer?.visibility = View.VISIBLE
            binding?.searchInputCard?.setStateFilling()
        }
    }


    fun applyMapFragment() {
        sharedState.saveState("SearchFragmentSelectedSubFragment", MapFragment::class)
        binding?.fragmentSearchMapFragment?.visibility = View.VISIBLE
        binding?.fragmentSearchListFragment?.visibility = View.GONE
    }

    fun applyListFragment() {
        sharedState.saveState("SearchFragmentSelectedSubFragment", ListFragment::class)
        binding?.fragmentSearchMapFragment?.visibility = View.GONE
        binding?.fragmentSearchListFragment?.visibility = View.VISIBLE
    }

    private fun listenKeyboardHeight() {
        val rootView: View = activity?.window?.decorView!!
        rootView.viewTreeObserver?.addOnGlobalLayoutListener {
            val r = Rect()
            rootView.getWindowVisibleDisplayFrame(r)
            if (view != null) {
                view?.height.let {
                    val fragmentHeight: Int = it!!
                    val newHeight: Int = fragmentHeight - (r.bottom - r.top)
                    heightOfKeyboard.value = newHeight
                }
            }

        }
    }

    private fun displayLoading() {
        isLoadingEnabled = true
        Handler(Looper.getMainLooper()).postDelayed(
            {
                if (isLoadingEnabled) {
                    binding?.fragmentSearchLoading?.visibility = View.VISIBLE
                    val d: Drawable = binding?.fragmentSearchLoading?.drawable!!
                    (d as? AnimatedVectorDrawable)?.start()
                }
            }, 500
        )
    }

    private fun hideLoading() {
        isLoadingEnabled = false
        binding?.fragmentSearchLoading?.visibility = View.GONE
        val d: Drawable = binding?.fragmentSearchLoading?.drawable!!
        (d as? AnimatedVectorDrawable)?.stop()
    }


    private fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
        observe(lifecycleOwner, object : Observer<T> {
            override fun onChanged(t: T?) {
                observer.onChanged(t)
                removeObserver(this)
            }
        })
    }

}
