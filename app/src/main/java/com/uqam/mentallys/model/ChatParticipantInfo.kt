package com.uqam.mentallys.model

import android.os.Parcelable
import androidx.room.Entity
import kotlinx.parcelize.Parcelize
import java.util.*

@Entity(tableName = "ChatParticipantInfo_tbl")
@Parcelize
data class ChatParticipantInfo(
    var threadId: String,
    val communicationId: String,
    val displayName:String,
    val avatar: String,
    val shareHistoryTime: Date,
    val lastMessage:String,
):Parcelable