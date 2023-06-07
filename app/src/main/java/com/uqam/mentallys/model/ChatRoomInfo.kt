package com.uqam.mentallys.model

import com.azure.android.communication.chat.ChatThreadClient


data class ChatRoomInfo(
    var threadId: String,
    var chatThreadClient: ChatThreadClient
)