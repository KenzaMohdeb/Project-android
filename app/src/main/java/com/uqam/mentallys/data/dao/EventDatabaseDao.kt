package com.uqam.mentallys.data.dao

import androidx.room.*
import com.uqam.mentallys.model.Event
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDatabaseDao {

    fun getEvents(
        searchQuery: String,
        startDate: Long,
        endDate: Long,
        selection: Boolean,
    ): Flow<List<Event>> =
        if (selection) {
            getEventsUserSelection(searchQuery, startDate, endDate)
        } else {
            getEventsDefaultSelection(searchQuery, startDate)
        }

    @Query("SELECT * FROM events_tbl WHERE event_start_date >= :startDate AND typeName  LIKE '%' || :searchQuery || '%' ORDER BY event_start_date DESC")
    fun getEventsDefaultSelection(searchQuery: String, startDate: Long): Flow<List<Event>>

    @Query("SELECT * FROM events_tbl WHERE event_start_date >= :startDate AND event_end_date <= :endDate AND typeName  LIKE '%' || :searchQuery || '%' ORDER BY event_start_date DESC")
    fun getEventsUserSelection(
        searchQuery: String,
        startDate: Long,
        endDate: Long,
    ): Flow<List<Event>>

    @Query("SELECT * FROM events_tbl WHERE id=:id")
    suspend fun getEventById(id: String): Event

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: Event): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateEvent(event: Event): Int

    @Delete
    suspend fun deleteEvent(event: Event): Int

    @Query("DELETE FROM events_tbl")
    suspend fun deleteAllEvents(): Int

}
