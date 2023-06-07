package com.uqam.mentallys.repository

import com.uqam.mentallys.data.dao.EventDatabaseDao
import com.uqam.mentallys.data.datasources.ContextDataSource
import com.uqam.mentallys.data.datasources.EventTypeDataSource
import com.uqam.mentallys.data.datasources.ModalityDataSource
import com.uqam.mentallys.data.datasources.TagDataSource
import com.uqam.mentallys.model.Event
import com.uqam.mentallys.model.EventType
import com.uqam.mentallys.model.Tag
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

//class EventRepository @Inject constructor(private val eventDatabaseDao: EventDatabaseDao, private val api: EventApi) {
class EventRepository @Inject constructor(
    private val eventDatabaseDao: EventDatabaseDao,
    private val eventTypeDataSource: EventTypeDataSource,
    private val eventContextDataSource: ContextDataSource,
    private val eventModalityDataSource: ModalityDataSource,
    private val eventTagDataSource: TagDataSource,
) : EventRepositoryInterface {

    override suspend fun addEvent(event: Event) {
        eventDatabaseDao.insertEvent(event)
    }

    override suspend fun updateEvent(event: Event) {
        eventDatabaseDao.updateEvent(event)
    }

    override suspend fun deleteEvent(event: Event) {
        eventDatabaseDao.deleteEvent(event)
    }

    override suspend fun deleteAllEvents() {
        eventDatabaseDao.deleteAllEvents()
    }

    override fun getAllEvents(
        searchQuery: String,
        eventStartDate: Long,
        eventEndDate: Long,
        selection: Boolean
    ): Flow<List<Event>> {
        return eventDatabaseDao.getEvents(searchQuery, eventStartDate, eventEndDate, selection)
            .flowOn(Dispatchers.IO).conflate()
    }

    override fun fetchEventTypes(): List<EventType> {
        return eventTypeDataSource.loadEventTypes()
    }

    override fun fetchContexts(): List<String> {
        return eventContextDataSource.loadContexts()
    }

    override fun fetchCommunitySupportModalities(): List<String> =
        eventModalityDataSource.loadCommunitySupportModalities()

    override fun fetchMaintenanceModalities(): List<String> =
        eventModalityDataSource.loadMaintenanceModalities()

    override fun fetchConsultationModalities(): List<String> =
        eventModalityDataSource.loadConsultationModalities()

    override fun fetchPsychotherapyModalities(): List<String> =
        eventModalityDataSource.loadPsychotherapyModalities()

    override fun fetchCommunitySupportOptions(): List<String> =
        eventModalityDataSource.loadCommunitySupportOptions()

    override fun fetchTagsMaintenance(): List<Tag> =
        eventTagDataSource.loadTagsEntretien()

    override fun fetchTagsConsultation(): List<Tag> =
        eventTagDataSource.loadTagsConsultation()

    override fun fetchTagsPsychotherapy(): List<Tag> =
        eventTagDataSource.loadTagsPsychotherapy()


// This function retrieves events from API and should be replaced by getAllEvents function

//   override suspend fun getAllEvents(searchQuery: String): DataOrException<Flow<List<Event>>, Boolean, Exception>{
//        val response = try {
//            api.getEvents(searchQuery = "")
//        } catch (e: Exception) {
//            return DataOrException(e = e)
//        }
//        return DataOrException(data = response)
//    }

}