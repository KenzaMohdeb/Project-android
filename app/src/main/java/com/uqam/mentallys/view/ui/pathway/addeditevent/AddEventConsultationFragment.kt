package com.uqam.mentallys.view.ui.pathway.addeditevent

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.uqam.mentallys.R
import com.uqam.mentallys.databinding.FragmentAddEventConsultationBinding
import com.uqam.mentallys.utils.exhaustive
import com.uqam.mentallys.utils.getDate
import dagger.hilt.android.AndroidEntryPoint

@Suppress("DEPRECATION")
@AndroidEntryPoint
class AddEventConsultationFragment : Fragment(R.layout.fragment_add_event_consultation) {
    private lateinit var binding: FragmentAddEventConsultationBinding
    val viewModel: AddEditEventViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentAddEventConsultationBinding.bind(view)

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
            imageEventType.setImageResource(viewModel.eventType!!.icon)

            eventDatePicker.setOnClickListener {
                showDatePicker()
            }

            // Get Modality data
            dropDownEventModality.setOnItemClickListener { parent, view, position, id ->
                var selectedModality = parent.getItemAtPosition(position)
                viewModel.eventModality = selectedModality.toString()
            }

            // Get Stakeholder name
            textEventStakeholder.doAfterTextChanged {
                viewModel.eventStakeholder = textEventStakeholder.text.toString()
            }

            // Get Job tag data
            chipGroup.setOnCheckedStateChangeListener { group, checkedId ->
                var tagName = group.findViewById<Chip>(checkedId[0]).text
                viewModel.eventTag = tagName.toString()
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
                            AddEventConsultationFragmentDirections.actionAddEventConsultationFragmentToSavedEventFragment()
                        findNavController().navigate(action)
                    }
                    is AddEditEventViewModel.AddEditPathwayEvent.ShowInvalidInputMessage -> {
                        Snackbar.make(requireView(), eventFromChannel.msg, Snackbar.LENGTH_LONG)
                            .show()
                    }
                }.exhaustive

            }
        }

        // Modality List
        val modalityList: MutableList<String> = ArrayList()
        val modalityItems = viewModel.fetchConsultationModalities()
        for (item in modalityItems) {
            modalityList.add(item)
        }
        val modeAdapter = ArrayAdapter(requireContext(), R.layout.item_dropdown_list, modalityList)
        (binding.dropDownEventModality as? AutoCompleteTextView)?.setAdapter(modeAdapter)

        // TAG List
        val tags = viewModel.tagItems()
        for (item in tags) {
            val chipItem = layoutInflater.inflate(R.layout.item_chip, null, false) as Chip
            chipItem.text = item.name
            chipItem.isCheckedIconVisible = true
            chipItem.isCheckable = true

            binding.chipGroup.addView(chipItem)
        }
    }

    private fun showDatePicker() {

        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Sélectionnez des dates")
                .build()

        datePicker.show(requireActivity().supportFragmentManager, "tag")

        datePicker.addOnPositiveButtonClickListener { datePicked ->
            val startDate = datePicked + 86400000
            viewModel.eventStartDate = startDate
            binding.eventDatePicker.text = getDate(startDate, "dd MMM yyyy")
        }
    }
}