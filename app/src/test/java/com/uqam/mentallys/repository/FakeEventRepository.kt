package com.uqam.mentallys.repository

import com.uqam.mentallys.R
import com.uqam.mentallys.model.*
import com.uqam.mentallys.utils.UUIDConverter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeEventRepository : EventRepositoryInterface {

    private var events = mutableListOf<Event>()
//    private val eventsLiveData = MutableLiveData<List<Event>>(events)
    val exampleEvent = Event(
        id = UUIDConverter().uuidFromString("c68b49b4-5286-4889-a10d-bb4dbc66949e"),
        name = EventType(
            typeId = UUIDConverter().uuidFromString("4c77a37d-6154-48d6-a376-e6d8f3a13a47"),
            typeName = EventTypeModels().maintenance,
            icon = R.drawable.entretien
        ),
        startDate = 1649828200000,
        endDate = 1664636400000
    )

    override suspend fun addEvent(event: Event) {
        events.add(event)
//        refreshData()
    }

    override suspend fun updateEvent(event: Event) {
        events.add(event)
    }

    override suspend fun deleteEvent(event: Event) {
        events.remove(event)
//        refreshData()
    }

    override suspend fun deleteAllEvents() {
        events.removeAll(events)
//        refreshData()
    }

    override fun getAllEvents(
        searchQuery: String,
        eventStartDate: Long,
        eventEndDate: Long,
        selection: Boolean
    ): Flow<List<Event>> = flow {
        events = mutableListOf(exampleEvent)
        emit(events)
//        return eventsLiveData.asFlow()
    }

    override fun fetchEventTypes(): List<EventType> {
        TODO("Not yet implemented")
    }

    override fun fetchContexts(): List<String> {
        TODO("Not yet implemented")
    }

    override fun fetchCommunitySupportModalities(): List<String> {
        TODO("Not yet implemented")
    }

    override fun fetchMaintenanceModalities(): List<String> {
        TODO("Not yet implemented")
    }

    override fun fetchConsultationModalities(): List<String> {
        TODO("Not yet implemented")
    }

    override fun fetchPsychotherapyModalities(): List<String> {
        TODO("Not yet implemented")
    }

    override fun fetchCommunitySupportOptions(): List<String> {
        TODO("Not yet implemented")
    }

    override fun fetchTagsMaintenance(): List<Tag> {
        TODO("Not yet implemented")
    }

    override fun fetchTagsConsultation(): List<Tag> {
        TODO("Not yet implemented")
    }

    override fun fetchTagsPsychotherapy(): List<Tag> {
        TODO("Not yet implemented")
    }
}