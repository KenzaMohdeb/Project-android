package com.uqam.mentallys.view.ui.pathway.eventsList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.uqam.mentallys.repository.EventRepositoryInterface
import com.uqam.mentallys.utils.getDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventListViewModel @Inject constructor(private val repository: EventRepositoryInterface) :
    ViewModel() {

    private val pathwayEventChannel = Channel<PathwayEvent>()
    val pathwayEvent = pathwayEventChannel.receiveAsFlow()

    val searchQuery = MutableStateFlow("")
    var eventStartDate = MutableStateFlow(value= System.currentTimeMillis() - 7776000000)
    var eventEndDate =  MutableStateFlow(value = System.currentTimeMillis())
    var selection: Boolean = false

    val eventsFlow = combine(
        searchQuery,
        eventStartDate,
        eventEndDate
    ) { query, startDate, endDate ->
        Triple(query, startDate, endDate)
    }.flatMapLatest { (query, startDate, endDate) ->
        repository.getAllEvents(query, startDate, endDate, selection)
    }
    val events = eventsFlow.asLiveData()

    val fromDate: String = getDate(System.currentTimeMillis() - 7776000000, "MMMM yyyy")
    val untilDate: String = getDate(System.currentTimeMillis(), "MMMM yyyy")

    fun onFabAddEventButtonClick() = viewModelScope.launch {
        pathwayEventChannel.send(PathwayEvent.NavigateToSelectEventTypeScreen)
    }

    private fun showEventSavedConfirmationMessage(text: String) = viewModelScope.launch {
        pathwayEventChannel.send(PathwayEvent.ShowEventSavedConfirmationMessage(text))
    }

    sealed class PathwayEvent() {
        object NavigateToSelectEventTypeScreen : PathwayEvent()
        data class ShowEventSavedConfirmationMessage(val msg: String) : PathwayEvent()
    }

}