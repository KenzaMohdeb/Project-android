package com.uqam.mentallys.data.dao

import androidx.annotation.Keep
import androidx.room.*
import com.uqam.mentallys.model.ChatResourceLoc
import com.uqam.mentallys.model.Event
import com.uqam.mentallys.model.resource.HistoryEntry
import com.uqam.mentallys.model.resource.Resource

@Dao
interface ResourceDatabaseDao {

   /* @Query("SELECT * FROM ChatDataSource WHERE id=:id")
    fun getResourceById(id: String): ChatResourceLoc*/

    @Keep
    @Query("SELECT * FROM resources")
    fun getResources(): List<Resource>

    @Keep
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addResource(resource: Resource)

    @Keep
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @JvmSuppressWildcards
    fun addResource(resources: List<Resource>)

    @Keep
    @Delete
    fun deleteResource(resource: Resource): Int

    @Keep
    @Query("DELETE FROM resources")
    fun deleteResource(): Int

    @Keep
    @Query("SELECT * FROM resources_history LIMIT 20")
    fun getHistory(): List<HistoryEntry>

    @Keep
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addHistoryEntry(historyEntry: HistoryEntry): Long

    @Keep
    @Query("DELETE FROM resources_history WHERE history_entry_id NOT IN (SELECT history_entry_id FROM resources_history ORDER BY date ASC LIMIT 20)")
    fun sweepHistory()

    @Keep
    @Delete
    fun deleteHistoryEntry(historyEntry: HistoryEntry): Int

    @Keep
    @Query("DELETE FROM resources_history")
    fun deleteHistory(): Int

}