package com.uqam.mentallys.model.resource

import androidx.annotation.Keep
import androidx.room.*
import java.time.OffsetDateTime
import java.util.*

@Keep
@Entity(tableName = "resources_history")

@SuppressWarnings(RoomWarnings.PRIMARY_KEY_FROM_EMBEDDED_IS_DROPPED)
data class HistoryEntry(
    @Embedded val resource: Resource,
    @ColumnInfo(name = "history_entry_id")
    @PrimaryKey val id: UUID = resource.id,
    @ColumnInfo(name = "date")
    val date: OffsetDateTime = OffsetDateTime.now(),
    )