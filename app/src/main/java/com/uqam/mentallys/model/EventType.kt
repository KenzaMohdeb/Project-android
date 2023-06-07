package com.uqam.mentallys.model

import android.os.Parcelable
import androidx.room.Entity
import kotlinx.parcelize.Parcelize
import java.util.*

@Entity(tableName = "event_type_tbl")
@Parcelize
data class EventType(
    val typeId: UUID = UUID.randomUUID(),
    val icon: Int,
    var typeName: String,
) : Parcelable
