package com.uqam.mentallys.view.ui.message

import android.graphics.Rect
import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.*
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.uqam.mentallys.R
import com.uqam.mentallys.databinding.FragmentIntervenantsBinding
import com.uqam.mentallys.utils.SharedState
import com.uqam.mentallys.view.ui.resource.common.CategoriesAdapter
import com.uqam.mentallys.view.ui.resource.common.ResourceHistoryAdapter
import com.uqam.mentallys.view.ui.resource.list.ListFragment
import com.uqam.mentallys.view.ui.resource.search.FilterBottomSheetFragment
import com.uqam.mentallys.viewmodels.resource.FilterViewModel
import com.uqam.mentallys.viewmodels.resource.ResourceViewModel
import dagger.hilt.android.AndroidEntryPoint


@Suppress("COMPATIBILITY_WARNING", "DEPRECATION")
@AndroidEntryPoint
class IntervenantsFragment : Fragment(R.layout.fragment_intervenants) {

    private lateinit var binding: FragmentIntervenantsBinding
    private val sharedState: SharedState = SharedState
    private val resourceViewModel: ResourceViewModel by viewModels()
    private val filterViewModel: FilterViewModel by viewModels()
    private val resourceCategoryAdapter = CategoriesAdapter()
    private var heightOfKeyboard: MutableLiveData<Int> = MutableLiveData(0)
    private lateinit var resourceHistoryAdapter: ResourceHistoryAdapter
    private var isLoadingEnabled: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (sharedState.getState("ResourcePackageOpenedResource") != null) {
            val bundle = Bundle()
            val item: com.uqam.mentallys.model.resource.Resource = sharedState.getState("ResourcePackageOpenedResource") as com.uqam.mentallys.model.resource.Resource
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
        binding = FragmentIntervenantsBinding.bind(view!!)
        restoreSharedState()
        saveState()
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Get an instance of the input manager and save it as class property fo further uses
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            resourceHistoryAdapter = ResourceHistoryAdapter {
                searchInputCard.keepFocus()
                searchInputCard.ignoreStateChange = true
            }
            setupBottomSheetFilter()
            setupHistory()
            setupAdvancedInputCard()
        }
    }
    private fun restoreSharedState() {
        if (sharedState.getState("SearchFragmentSelectedSubFragment") == null) {
            applyMapFragment()
        } else {
            when (sharedState.getState("SearchFragmentSelectedSubFragment")!!) {
                ListIntervenantsFragment::class -> applyMapFragment()
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
            binding.searchInputCard.setInputText(filterViewModel.filter.value?.searchString.toString())
        }
        if (sharedState.getState("SearchFragmentIsHistoryOpened") == true) {
            binding.fragmentSearchHistoryContainer.visibility = View.VISIBLE
            binding.searchInputCard.setStateFilling()
        }
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

    private fun setBackEventListener() {
        activity?.onBackPressedDispatcher?.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (filterViewModel.filter.value?.isThereAnyActiveFilter() == true) {
                    filterViewModel.filter.value?.reset()
                    binding.searchInputCard.setInputText("")
                    binding.searchInputCard.setStateEmpty()
                    resourceCategoryAdapter.selectedCategory.value = listOf()
                }else {
                    when (sharedState.getState("SearchFragmentSelectedSubFragment")) {
                        ListFragment::class -> this@IntervenantsFragment.applyMapFragment()
                        else -> findNavController().popBackStack()
                    }
                }
            }
        })
    }
    fun applyMapFragment() {
        sharedState.saveState("SearchFragmentSelectedSubFragment", ListIntervenantsFragment::class)
        binding.fragmentSearchIntervantFragment.visibility = View.VISIBLE
        binding.fragmentSearchListFragment.visibility = View.GONE
    }

    fun applyListFragment() {
        sharedState.saveState("SearchFragmentSelectedSubFragment", ListFragment::class)
        binding.fragmentSearchIntervantFragment.visibility = View.GONE
        binding.fragmentSearchListFragment.visibility = View.VISIBLE
    }

    private fun setupBottomSheetFilter() {
        // Bottom sheet filter setup
        binding.apply {
            searchInputCard.onFilterButtonClickCallBack = {
                val bottomSheetDialog = FilterBottomSheetFragment()
                if (childFragmentManager.findFragmentByTag("FilterBottomSheet") == null) {
                    bottomSheetDialog.show(childFragmentManager, "FilterBottomSheet")
                    bottomSheetDialog.isCancelable = true
                    bottomSheetDialog.validateFilterCallback = {
                        //displayLoading()
                        resourceViewModel.list.observeOnce(viewLifecycleOwner) {
                            //centerOnResult()
                            hideLoading()
                        }
                    }
                }
            }
        }
    }

    private fun setupAdvancedInputCard() {
        binding.apply {
            // Search bar listener setup
            searchInputCard.onTextUpdateCallBack = { text ->
                filterViewModel.filter.value?.searchString = text
            }
            searchInputCard.onEnterPressedCallback = { _, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN) {
                    val filter = filterViewModel.filter.value
                    filterViewModel.filter.value = filter
                   // displayLoading()
                    resourceViewModel.list.observeOnce(viewLifecycleOwner) {
                       // centerOnResult()
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
    private fun hideLoading() {
        isLoadingEnabled = false
        binding.fragmentSearchLoading.visibility = View.GONE
        val d: Drawable = binding.fragmentSearchLoading.drawable!!
        (d as? AnimatedVectorDrawable)?.stop()
    }

    private fun saveState() {
        binding.apply {
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
    private fun setupHistory() {
        resourceViewModel.getHistory()
        binding.apply {
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
    private fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
        observe(lifecycleOwner, object : Observer<T> {
            override fun onChanged(t: T?) {
                observer.onChanged(t)
                removeObserver(this)
            }
        })
    }
}