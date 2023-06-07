package com.uqam.mentallys.model

import com.azure.android.communication.chat.ChatThreadClient


data class ListChatThreadClients(
    var listChatClient:ArrayList<ChatThreadClient>,
    var errorMessage:String
)