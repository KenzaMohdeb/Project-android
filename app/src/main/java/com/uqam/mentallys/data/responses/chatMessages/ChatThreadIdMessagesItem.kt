package com.uqam.mentallys.data.responses.chatMessages

data class ChatThreadIdMessagesItem(
    val content: Content,
    val createdOn: String,
    val deletedOn: Any,
    val editedOn: Any,
    val id: String,
    val metadata: Metadata,
    val sender: Sender,
    val senderDisplayName: String,
    val sequenceId: String,
    val type: Type,
    val version: String
)