package com.uqam.mentallys.data.responses.chatMessages

data class Participant(
    val displayName: String,
    val shareHistoryTime: String,
    val user: User
)