package com.uqam.mentallys.data.responses

data class Content(
    val initiator: Initiator,
    val message: String,
    val participants: List<Participant>,
    val topic: String
)