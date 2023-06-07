package com.uqam.mentallys.viewmodels.pathway

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uqam.mentallys.model.Event
import com.uqam.mentallys.model.EventType
import com.uqam.mentallys.model.EventTypeModels
import com.uqam.mentallys.model.Tag
import com.uqam.mentallys.repository.EventRepositoryInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditEventViewModel @Inject constructor(
    private val eventRepository: EventRepositoryInterface,
    private val state: SavedStateHandle?,
) : ViewModel() {

//    val event = state.get<Event>("event")

    val eventType = state!!.get<EventType>("eventType")

    //    var eventStartDate = state.get<Long>("eventStartDate") ?: event?.startDate ?: ""
    var eventStartDate = state?.get<Long>("eventStartDate") ?: ""
        set(value) {
            field = value
            state?.set("eventStartDate", value)
        }

    var eventEndDate = state?.get<Long>("eventEndDate") ?: ""
        set(value) {
            field = value
            state?.set("eventEndDate", value)
        }

    var eventModality = state?.get<String>("eventModality") ?: ""
        set(value) {
            field = value
            state?.set("eventModality", value)
        }

    var eventContext = state?.get<String>("eventContext") ?: ""
        set(value) {
            field = value
            state?.set("eventContext", value)
        }

    var eventStakeholder = state?.get<String>("eventStakeholder") ?: ""
        set(value) {
            field = value
            state?.set("eventStakeholder", value)
        }

    var eventPlace = state?.get<String>("eventPlace") ?: ""
        set(value) {
            field = value
            state?.set("eventPlace", value)
        }

    var eventTag = state?.get<String>("eventTag") ?: ""
        set(value) {
            field = value
            state?.set("eventTag", value)
        }

    var eventPrivateNote = state?.get<String>("eventPrivateNote") ?: ""
        set(value) {
            field = value
            state?.set("eventPrivateNote", value)
        }

    private val addEditEventChannel = Channel<AddEditPathwayEvent>()
    val addEditEvent = addEditEventChannel.receiveAsFlow()

//    fun addEvent(event: Event) = viewModelScope.launch { repository.addEvent(event) }
//    fun updateEvent(event: Event) = viewModelScope.launch { repository.updateEvent(event) }
//    fun deleteEvent(event: Event) = viewModelScope.launch { repository.deleteEvent(event) }

    fun onSaveClick() {

        when (eventType!!.typeName) {
            EventTypeModels().maintenance -> {
                if (eventStartDate == "" ||
                    eventModality == "" ||
                    eventContext == "" ||
                    eventStakeholder == "" ||
                    eventTag == ""
                ) {
//                    Log.d("tag_name", eventType.icon.toString())
                    showInvalidInputMessage("Remplir tous les champs étoilés est obligatoire")
                    return
                } else {
                    val newEvent = Event(
                        name = eventType,
                        startDate = eventStartDate as Long,
                        modality = eventModality,
                        context = eventContext,
                        stakeholder = eventStakeholder,
                        jobTag = eventTag,
                        privateNote = eventPrivateNote
                    )
                    createEvent(newEvent)
                }
            }
            EventTypeModels().consultation -> {
                if (eventStartDate == "" ||
                    eventModality == "" ||
                    eventStakeholder == "" ||
                    eventTag == ""
                ) {
//                    Log.d("tag_name", eventType.icon.toString())
                    showInvalidInputMessage("Remplir tous les champs étoilés est obligatoire")
                    return
                } else {
                    val newEvent = Event(
                        name = eventType,
                        startDate = eventStartDate as Long,
                        modality = eventModality,
                        stakeholder = eventStakeholder,
                        jobTag = eventTag,
                        privateNote = eventPrivateNote
                    )
                    createEvent(newEvent)
                }
            }
            EventTypeModels().psychotherapy -> {
                if (eventStartDate == "" ||
                    eventEndDate == "" ||
                    eventModality == "" ||
                    eventStakeholder == "" ||
                    eventTag == ""
                ) {
                    showInvalidInputMessage("Remplir tous les champs étoilés est obligatoire")
                    return
                } else {
                    val newEvent = Event(
                        name = eventType,
                        startDate = eventStartDate as Long,
                        endDate = eventEndDate as Long,
                        modality = eventModality,
                        stakeholder = eventStakeholder,
                        jobTag = eventTag,
                        privateNote = eventPrivateNote
                    )
                    createEvent(newEvent)
                }
            }
            EventTypeModels().Hospitalization -> {
                if (eventStartDate == "" ||
                    eventEndDate == "" ||
                    eventPlace == ""
                ) {
                    showInvalidInputMessage("Remplir tous les champs étoilés est obligatoire")
                    return
                } else {
                    val newEvent = Event(
                        name = eventType,
                        startDate = eventStartDate as Long,
                        endDate = eventEndDate as Long,
                        place = eventPlace,
                        privateNote = eventPrivateNote
                    )
                    createEvent(newEvent)
                }
            }
            EventTypeModels().CommunitySupport -> {
                if (eventStartDate == "" ||
                    eventModality == "" ||
                    eventPlace == "" ||
                    eventContext == ""
                ) {
                    showInvalidInputMessage("Remplir tous les champs étoilés est obligatoire")
                    return
                } else {
                    val newEvent = Event(
                        name = eventType,
                        startDate = eventStartDate as Long,
                        endDate = eventEndDate as Long,
                        modality = eventModality,
                        place = eventPlace,
                        context = eventContext,
                        privateNote = eventPrivateNote
                    )
                    createEvent(newEvent)
                }
            }
            EventTypeModels().Accommodation -> {
                if (eventStartDate == "" ||
                    eventEndDate == "" ||
                    eventPlace == ""
                ) {
                    showInvalidInputMessage("Remplir tous les champs étoilés est obligatoire")
                    return
                } else {
                    val newEvent = Event(
                        name = eventType,
                        startDate = eventStartDate as Long,
                        endDate = eventEndDate as Long,
                        place = eventPlace,
                        privateNote = eventPrivateNote
                    )
                    createEvent(newEvent)
                }
            }
            EventTypeModels().Other -> {
                if (eventStartDate == "" ||
                    eventPlace == ""
                ) {
//                    Log.d("tag_name", eventPrivateNote.toString())
                    showInvalidInputMessage("Remplir tous les champs étoilés est obligatoire")
                    return
                } else {
                    val newEvent = Event(
                        name = eventType,
                        startDate = eventStartDate as Long,
                        endDate = eventEndDate as Long,
                        place = eventPlace,
                        privateNote = eventPrivateNote
                    )
                    createEvent(newEvent)
                }
            }
        }
    }

    fun createEvent(event: Event) = viewModelScope.launch {
        eventRepository.addEvent(event)
        addEditEventChannel.send(AddEditPathwayEvent.NavigateToSavedEventScreen)
    }

    private fun showInvalidInputMessage(text: String) = viewModelScope.launch {
        addEditEventChannel.send(AddEditPathwayEvent.ShowInvalidInputMessage(text))
    }

    fun tagItems(): List<Tag> {
        when (eventType!!.typeName) {
            EventTypeModels().maintenance -> {
                return eventRepository.fetchTagsMaintenance()
            }
            EventTypeModels().consultation -> {
                return eventRepository.fetchTagsConsultation()
            }
            EventTypeModels().psychotherapy -> {
                return eventRepository.fetchTagsPsychotherapy()
            }
        }
        return emptyList()
    }

    fun fetchContexts(): List<String> {
        return eventRepository.fetchContexts()
    }

    fun fetchCommunitySupportModalities(): List<String> {
        return eventRepository.fetchCommunitySupportModalities()
    }

    fun fetchMaintenanceModalities(): List<String> {
        return eventRepository.fetchMaintenanceModalities()
    }

    fun fetchConsultationModalities(): List<String> {
        return eventRepository.fetchConsultationModalities()
    }

    fun fetchPsychotherapyModalities(): List<String> {
        return eventRepository.fetchPsychotherapyModalities()
    }

    fun fetchCommunitySupportOptions(): List<String> {
        return eventRepository.fetchCommunitySupportOptions()
    }

    sealed class AddEditPathwayEvent {
        data class ShowInvalidInputMessage(val msg: String) : AddEditPathwayEvent()
        object NavigateToSavedEventScreen : AddEditPathwayEvent()
    }
}
