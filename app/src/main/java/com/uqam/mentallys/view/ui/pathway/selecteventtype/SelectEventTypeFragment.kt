package com.uqam.mentallys.view.ui.pathway.selecteventtype

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.uqam.mentallys.R
import com.uqam.mentallys.databinding.FragmentEventTypeBinding
import com.uqam.mentallys.model.EventType
import com.uqam.mentallys.model.EventTypeModels
import com.uqam.mentallys.utils.exhaustive
import dagger.hilt.android.AndroidEntryPoint

@Suppress("DEPRECATION")
@AndroidEntryPoint
class SelectEventTypeFragment : Fragment(R.layout.fragment_event_type),
    SelectEventTypeAdapter.OnItemClickListener {

    private val viewModel: SelectEventTypeViewModel by viewModels()
    private val eventTypeAdapter = SelectEventTypeAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.window?.statusBarColor = resources.getColor(R.color.grey)

        val binding = FragmentEventTypeBinding.bind(view)

        // Menu Section
        binding.apply {
            eventTypeToolbar.inflateMenu(R.menu.top_menu_event_type_fragment)
            eventTypeToolbar.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.close_event_type -> {
                        findNavController().navigateUp()
                        true
                    }
                    else -> false
                }
            }
        }

        viewModel.fetchEventTypes()

        binding.apply {
            recyclerViewEventTypes.apply {
                adapter = eventTypeAdapter
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                setHasFixedSize(true)
            }

        }
        observeEventTypesViewModel()

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.selectEventType.collect { eventFromChannel ->
                when (eventFromChannel) {
                    is SelectEventTypeViewModel.SelectEventTypeEvent.NavigateToAddEditEventScreen -> {
                        when (eventFromChannel.eventType.typeName) {
                            EventTypeModels().maintenance -> {
                                val action =
                                    SelectEventTypeFragmentDirections.actionSelectEventTypeFragmentToAddEventMaintenanceFragment(
                                        eventFromChannel.eventType)
                                findNavController().navigate(action)
                            }
                            EventTypeModels().consultation -> {
                                val action =
                                    SelectEventTypeFragmentDirections.actionSelectEventTypeFragmentToAddEventConsultationFragment(
                                        eventFromChannel.eventType)
                                findNavController().navigate(action)
                            }
                            EventTypeModels().psychotherapy -> {
                                val action =
                                    SelectEventTypeFragmentDirections.actionSelectEventTypeFragmentToAddEventPsychotherapyFragment(
                                        eventFromChannel.eventType)
                                findNavController().navigate(action)
                            }
                            EventTypeModels().Hospitalization -> {
                                val action =
                                    SelectEventTypeFragmentDirections.actionSelectEventTypeFragmentToAddEventHospitalizationFragment(
                                        eventFromChannel.eventType)
                                findNavController().navigate(action)
                            }
                            EventTypeModels().CommunitySupport -> {
                                val action =
                                    SelectEventTypeFragmentDirections.actionSelectEventTypeFragmentToAddEventCommunitySupportFragment(
                                        eventFromChannel.eventType)
                                findNavController().navigate(action)
                            }
                            EventTypeModels().Accommodation -> {
                                val action =
                                    SelectEventTypeFragmentDirections.actionSelectEventTypeFragmentToAddEventAccommodationFragment(
                                        eventFromChannel.eventType)
                                findNavController().navigate(action)
                            }
                            EventTypeModels().Other -> {
                                val action =
                                    SelectEventTypeFragmentDirections.actionSelectEventTypeFragmentToAddEventOtherFragment(
                                        eventFromChannel.eventType)
                                findNavController().navigate(action)
                            }
                            else -> {
                                Snackbar.make(requireView(),
                                    "Veuillez réessayer",
                                    Snackbar.LENGTH_LONG)
                                    .show()
                            }
                        }

                    }
                }.exhaustive
            }
        }

    }

    private fun observeEventTypesViewModel() {
        viewModel.eventTypes.observe(viewLifecycleOwner) { eventTypes ->
            eventTypeAdapter.submitList(eventTypes)
        }
    }

    override fun onItemClick(eventType: EventType) {
        viewModel.onEventTypeSelected(eventType)
    }

}