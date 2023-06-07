package com.uqam.mentallys.data.responses

data class ChatSessionInfo(
    val  participantIdentity: String?,
    val  participantToken: String?,
    val  participantName: String?,
    val  threadId: String?,
    val  endPoint: String?,
    val  errorMessage: String?,

)