package com.uqam.mentallys.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.uqam.mentallys.data.dao.EventDatabaseDao
import com.uqam.mentallys.data.dao.ResourceDatabaseDao
import com.uqam.mentallys.model.Event
import com.uqam.mentallys.model.resource.HistoryEntry
import com.uqam.mentallys.model.resource.Resource
import com.uqam.mentallys.utils.OffsetDateConverter
import com.uqam.mentallys.utils.UUIDConverter
import com.uqam.mentallys.utils.ResourceConverter


@Database(entities = [Event::class, Resource::class, HistoryEntry::class],
    version = 3,
    exportSchema = false)
@TypeConverters(UUIDConverter::class, ResourceConverter::class, OffsetDateConverter::class)
abstract class MentallysDatabase : RoomDatabase() {
    abstract fun eventDatabaseDao(): EventDatabaseDao
    abstract fun resourceHistoryDao(): ResourceDatabaseDao
}