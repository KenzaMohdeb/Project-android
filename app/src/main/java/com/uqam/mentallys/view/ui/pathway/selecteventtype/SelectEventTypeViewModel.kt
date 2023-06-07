package com.uqam.mentallys.view.ui.pathway.selecteventtype

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uqam.mentallys.model.EventType
import com.uqam.mentallys.repository.EventRepositoryInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelectEventTypeViewModel @Inject constructor(
    private val repository: EventRepositoryInterface,
    private val state: SavedStateHandle
) : ViewModel() {

    val eventTypes = MutableLiveData<List<EventType>>()
    private val selectEventTypeChannel = Channel<SelectEventTypeEvent>()
    val selectEventType = selectEventTypeChannel.receiveAsFlow()

    fun fetchEventTypes() {
        eventTypes.value = repository.fetchEventTypes()
    }


    fun onEventTypeSelected(eventType: EventType) = viewModelScope.launch{
        selectEventTypeChannel.send(SelectEventTypeEvent.NavigateToAddEditEventScreen(eventType))
    }

    sealed class SelectEventTypeEvent {
        data class NavigateToAddEditEventScreen(val eventType: EventType) : SelectEventTypeEvent()
    }

}