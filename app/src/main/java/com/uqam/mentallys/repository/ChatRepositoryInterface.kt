package com.uqam.mentallys.repository

import com.azure.android.communication.chat.ChatClient
import com.azure.android.communication.chat.ChatThreadClient
import com.mentallys.chatView.model.ChatMessage
import com.uqam.mentallys.model.ChatParticipantInfo
import com.uqam.mentallys.model.ChatResourceLoc
import com.uqam.mentallys.model.ListChatThreadClients

interface ChatRepositoryInterface {
    fun fetch(): List<ChatResourceLoc>
    fun getResourceByEmail(email: String): ChatResourceLoc?
    fun getResourceByCommunicationUserId(CommunicationUserId: String): ChatResourceLoc?
    fun getListChatThreadClient(chatClient:ChatClient): ListChatThreadClients
    fun getExistingChatRoomThreadId(listChatThreadClients: ListChatThreadClients,
                                    communicationInvitedUserId:String, userId: String ): List<ChatParticipantInfo>
    fun createNewChatThreadId(chatClient:ChatClient, userID:String, userName:String, topic:String):String

    fun getChatMessages(chatThreadClient: ChatThreadClient, userId:String):List<ChatMessage>

}