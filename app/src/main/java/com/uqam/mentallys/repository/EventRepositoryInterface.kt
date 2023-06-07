package com.uqam.mentallys.repository

import com.uqam.mentallys.model.Event
import com.uqam.mentallys.model.EventType
import com.uqam.mentallys.model.Tag
import kotlinx.coroutines.flow.Flow

interface EventRepositoryInterface {
    suspend fun addEvent(event: Event)
    suspend fun updateEvent(event: Event)
    suspend fun deleteEvent(event: Event)
    suspend fun deleteAllEvents()
    fun getAllEvents(
        searchQuery: String,
        eventStartDate: Long,
        eventEndDate: Long,
        selection: Boolean,
    ): Flow<List<Event>>

    fun fetchEventTypes(): List<EventType>
    fun fetchContexts(): List<String>
    fun fetchCommunitySupportModalities(): List<String>
    fun fetchMaintenanceModalities(): List<String>
    fun fetchConsultationModalities(): List<String>
    fun fetchPsychotherapyModalities(): List<String>
    fun fetchCommunitySupportOptions(): List<String>
    fun fetchTagsMaintenance(): List<Tag>
    fun fetchTagsConsultation(): List<Tag>
    fun fetchTagsPsychotherapy(): List<Tag>
}