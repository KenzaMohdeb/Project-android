package com.uqam.mentallys.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.*

@Entity(tableName = "events_tbl")
@Parcelize
data class Event(
    @PrimaryKey
    val id: UUID = UUID.randomUUID(),

    @Embedded
    val name: EventType,

    @ColumnInfo(name = "event_start_date")
    val startDate: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "event_end_date")
    val endDate: Long? = null,

    @ColumnInfo(name = "event_modality")
    val modality: String? = null,

    @ColumnInfo(name = "event_context")
    val context: String? = null,

    @ColumnInfo(name = "event_stakeholder")
    val stakeholder: String? = null,

    @ColumnInfo(name = "event_tag")
    val jobTag: String? = null,

    @ColumnInfo(name = "event_place")
    val place: String? = null,

    @ColumnInfo(name = "event_private_note")
    val privateNote: String? = null,

    ): Parcelable
