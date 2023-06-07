package com.uqam.mentallys.data.responses

import com.azure.android.communication.chat.implementation.models.ChatMessageContent
import com.azure.android.communication.chat.models.ChatMessageType
import com.azure.android.communication.common.CommunicationIdentifier
import java.util.Date

data class ChatMessages(
    val id: String?,
    val type: ChatMessageType?,
    val sequenceId: String?,
    val version: String?,
    val content: ChatMessageContent?,
    val senderDisplayName:String,
    val createdOn:Date,
    val sender: CommunicationIdentifier,
    val deletedOn:Date,
    val editedOn:Date,
)