package com.uqam.mentallys.data.responses

data class ResponseChatMessages(
    val content: Content,
    val createdOn: String,
    val deletedOn: String,
    val editedOn: String,
    val id: String,
    val metadata: Metadata,
    val sender: Sender,
    val senderDisplayName: String,
    val sequenceId: String,
    val type: Type,
    val version: String
)