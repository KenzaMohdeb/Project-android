package com.uqam.mentallys.view.ui.pathway.addeditevent

import android.os.Bundle
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.uqam.mentallys.R
import com.uqam.mentallys.databinding.FragmentAddEventAccommodationBinding
import com.uqam.mentallys.utils.exhaustive
import com.uqam.mentallys.utils.getDate
import dagger.hilt.android.AndroidEntryPoint

@Suppress("DEPRECATION")
@AndroidEntryPoint
class AddEventAccommodationFragment : Fragment(R.layout.fragment_add_event_accommodation) {
    private lateinit var binding: FragmentAddEventAccommodationBinding
    val viewModel: AddEditEventViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAddEventAccommodationBinding.bind(view)

        binding.apply {

            // Top Menu Section ************************************************
            addEventToolbar.inflateMenu(R.menu.top_menu_add_event_fragments)
            addEventToolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
            addEventToolbar.setNavigationOnClickListener {
                findNavController().navigateUp()
            }
            addEventToolbar.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.close_event -> {
                        findNavController().navigate(R.id.pathwayFragment)
                        true
                    }
                    else -> false
                }
            }
            // *******************************************************

            textEventType.text = viewModel.eventType!!.typeName
            viewModel.eventType!!.icon.let { imageEventType.setImageResource(it) }

            eventStartDatePicker.setOnClickListener {
                showDateRangePicker()
            }

            // Get Accommodation name
            textEventAccommodationPlace.doAfterTextChanged {
                viewModel.eventPlace = textEventAccommodationPlace.text.toString()
            }

            // Get Private Note
            textPrivateNote.doAfterTextChanged {
                viewModel.eventPrivateNote = textPrivateNote.text.toString()
            }

            fabSaveEvent.setOnClickListener {
                viewModel.onSaveClick()
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.addEditEvent.collect { eventFromChannel ->
                when (eventFromChannel) {
                    is AddEditEventViewModel.AddEditPathwayEvent.NavigateToSavedEventScreen -> {
                        val action =
                            AddEventAccommodationFragmentDirections.actionAddEventAccommodationFragmentToSavedEventFragment()
                        findNavController().navigate(action)
                    }
                    is AddEditEventViewModel.AddEditPathwayEvent.ShowInvalidInputMessage -> {
                        Snackbar.make(requireView(), eventFromChannel.msg, Snackbar.LENGTH_LONG)
                            .show()
                    }
                }.exhaustive
            }
        }
    }

    private fun showDateRangePicker() {

        val dateRangePicker = MaterialDatePicker.Builder.dateRangePicker()
            .setTitleText("Sélectionnez des dates")
            .build()

        dateRangePicker.show(requireActivity().supportFragmentManager, "tag")

        dateRangePicker.addOnPositiveButtonClickListener { datePicked ->
            val startDate = datePicked.first + 86400000
            val endDate = datePicked.second + 86400000

            viewModel.eventStartDate = startDate
            viewModel.eventEndDate = endDate

            binding.eventStartDatePicker.text = getDate(startDate, "dd MMM yyyy")
            binding.eventEndDatePicker.text = getDate(endDate, "dd MMM yyyy")
        }
    }
}