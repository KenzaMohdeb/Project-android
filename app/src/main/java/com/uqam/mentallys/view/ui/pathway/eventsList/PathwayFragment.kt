package com.uqam.mentallys.view.ui.pathway.eventsList

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.uqam.mentallys.R
import com.uqam.mentallys.databinding.FragmentPathwayBinding
import com.uqam.mentallys.model.Event
import com.uqam.mentallys.utils.exhaustive
import com.uqam.mentallys.utils.getDate
import com.uqam.mentallys.utils.onQueryTextChanged
import dagger.hilt.android.AndroidEntryPoint

@Suppress("DEPRECATION")
@AndroidEntryPoint
class PathwayFragment : Fragment(R.layout.fragment_pathway), EventsAdapter.OnItemClickListener {

    lateinit var binding: FragmentPathwayBinding

    private val viewModel: EventListViewModel by viewModels()

    private val eventAdapter = EventsAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.window?.statusBarColor = Color.WHITE
//        viewModel.selection = false

        binding = FragmentPathwayBinding.bind(view)

        observeViewModel()

        binding.apply {

            // Setting up recycler view
            recyclerViewEvents.apply {
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                adapter = eventAdapter
                setHasFixedSize(true)
            }

            // Setting header date in pathway page
            setHeaderDate(viewModel.fromDate, viewModel.untilDate)

            // Searching an Event
            textSearchView.onQueryTextChanged {
                viewModel.searchQuery.value = it
            }

            // Showing calendar
            dateSection.setOnClickListener {
                showDateRangePicker()
            }

            // Add new event
            fabAddEvent.setOnClickListener {
                viewModel.onFabAddEventButtonClick()
            }

        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.pathwayEvent.collect { eventFromChannel ->
                when (eventFromChannel) {
                    is EventListViewModel.PathwayEvent.NavigateToSelectEventTypeScreen -> {
                        val action =
                            PathwayFragmentDirections.actionPathwayFragmentToSelectEventTypeFragment()
                        findNavController().navigate(action)
                    }
                    is EventListViewModel.PathwayEvent.ShowEventSavedConfirmationMessage -> {
                        Snackbar.make(requireView(), eventFromChannel.msg, Snackbar.LENGTH_LONG)
                            .show()
                    }
                }.exhaustive
            }
        }
    }

    private fun observeViewModel() {
        viewModel.events.observe(viewLifecycleOwner) {
            eventAdapter.submitList(it)
        }
    }

    private fun showDateRangePicker() {
        val dateRangePicker =
            MaterialDatePicker.Builder.dateRangePicker()
                .setTitleText("Sélectionnez des dates")
                .build()

        dateRangePicker.show(requireActivity().supportFragmentManager, "DATE_PICKER")

        dateRangePicker.addOnPositiveButtonClickListener { datePicked ->
            val startDate = datePicked.first + 86400000
            val endDate = datePicked.second + 86400000

            viewModel.eventStartDate.value = startDate
            viewModel.eventEndDate.value = endDate
            viewModel.selection = true
            // Setting header date in pathway page
            setHeaderDate(getDate(startDate, "MMMM yyyy"), getDate(endDate, "MMMM yyyy"))
        }
    }

    private fun setHeaderDate(startDate: String, endDate: String) {
        binding.textFromDateValue.text = startDate
        binding.textUntilDateValue.text = endDate
    }

    override fun onItemClick(event: Event) {
//        viewModel.onEventSelected(event)
    }

}