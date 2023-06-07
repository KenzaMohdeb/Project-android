package com.uqam.mentallys.utils

import androidx.room.TypeConverter
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneOffset

class OffsetDateConverter {
    @TypeConverter
    fun toDate(dateLong: Long?): OffsetDateTime? {
        return OffsetDateTime.ofInstant(dateLong?.let { Instant.ofEpochSecond(it, 0) },
            ZoneOffset.UTC)
    }

    @TypeConverter
    fun fromDate(date: OffsetDateTime?): Long? {
        return date?.toInstant()?.epochSecond
    }
}