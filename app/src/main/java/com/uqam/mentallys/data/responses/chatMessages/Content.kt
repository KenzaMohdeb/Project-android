package com.uqam.mentallys.data.responses.chatMessages

data class Content(
    val initiator: Initiator,
    val message: String,
    val participants: List<Participant>,
    val topic: String
)