package com.uqam.mentallys.repository

import com.azure.android.communication.chat.ChatClient
import com.azure.android.communication.chat.ChatThreadClient
import com.azure.android.communication.chat.models.*
import com.azure.android.communication.common.CommunicationUserIdentifier
import com.mentallys.chatView.widget.ChatView
import com.uqam.mentallys.data.datasources.ChatDataSourceLocal
import com.uqam.mentallys.model.ChatParticipantInfo
import com.uqam.mentallys.model.ChatResourceLoc
import com.uqam.mentallys.model.ListChatThreadClients
import okhttp3.internal.UTC
import java.text.SimpleDateFormat
import javax.inject.Inject

class ChatRepository @Inject constructor(
    private val chatDataSourceLocal: ChatDataSourceLocal
) : ChatRepositoryInterface {

    override fun fetch(): List<ChatResourceLoc> {
        return chatDataSourceLocal.load()
    }
    override fun getResourceByEmail(email: String): ChatResourceLoc? {
        return chatDataSourceLocal.getResourceByEmail(email)
    }

    override fun getResourceByCommunicationUserId(CommunicationUserId: String): ChatResourceLoc? {
        return chatDataSourceLocal.getResourceByCommunicationUserId(CommunicationUserId)
    }

    override fun getListChatThreadClient(chatClient:ChatClient): ListChatThreadClients {

        val listChatThreadClients=ListChatThreadClients(mutableListOf<ChatThreadClient>() as ArrayList<ChatThreadClient>,"success")

        try {
            val listUserThreads = chatClient.listChatThreads()

            listUserThreads.byPage().forEach {
                it.value.forEach { chatThreadItem: ChatThreadItem ->
                    val chatThreadClient = chatClient.getChatThreadClient(chatThreadItem.id)
                    listChatThreadClients.listChatClient.add(chatThreadClient)
                }
            }
            return listChatThreadClients
        }
        catch(err:Throwable){
            listChatThreadClients.errorMessage = "error"
            if(err.message!!.contains("401") )
                listChatThreadClients.errorMessage = "401"
            return listChatThreadClients
        }
    }
    override fun getExistingChatRoomThreadId(listChatThreadClients: ListChatThreadClients,
                                             communicationInvitedUserId:String, userId: String ):List<ChatParticipantInfo> {
        val listChatThreadClient = mutableListOf<ChatParticipantInfo>()

        listChatThreadClients.listChatClient.forEach { chatThreadClientItem ->
            val threadParticipants = chatThreadClientItem.listParticipants()
            threadParticipants.byPage().forEach {

                it.value.forEach { chatParticipant: ChatParticipant ->

                    val dateFormat = setChatDataFormat()
                    val dataTime = dateFormat.parse(
                        chatParticipant.shareHistoryTime.toLocalDateTime().toString()
                    )!!

                    if((chatParticipant.communicationIdentifier as CommunicationUserIdentifier).id != userId){
                    if ((chatParticipant.communicationIdentifier as CommunicationUserIdentifier).id == communicationInvitedUserId) {



                        listChatThreadClient.add(
                            0, ChatParticipantInfo(
                                chatThreadClientItem.chatThreadId,
                                (chatParticipant.communicationIdentifier as CommunicationUserIdentifier).id,
                                chatParticipant.displayName,
                                "",
                                dataTime,
                                lastMessage = "last message"
                            )
                        )
                    } else {
                        listChatThreadClient.add(
                            ChatParticipantInfo(
                                chatThreadClientItem.chatThreadId,
                                (chatParticipant.communicationIdentifier as CommunicationUserIdentifier).id,
                                chatParticipant.displayName,
                                "",
                                dataTime,
                                lastMessage = "last message"
                            )
                        )
                    }
                }
                }
            }
        }
        return listChatThreadClient
    }
    override fun getChatMessages(
        chatThreadClient: ChatThreadClient,
        userId:String):List<com.mentallys.chatView.model.ChatMessage> {

        val messages= mutableListOf<com.mentallys.chatView.model.ChatMessage>()
        val chatMessages = chatThreadClient.listMessages()
        chatMessages.byPage().forEach {
            it.value.forEach { chatMessage: ChatMessage? ->
                if (chatMessage != null && chatMessage.senderCommunicationIdentifier != null) {
                    val authorID =
                        (chatMessage.senderCommunicationIdentifier as CommunicationUserIdentifier).id.toString()

                    var isSender=false
                    val dateFormat = setChatDataFormat()
                    val dataTime = dateFormat.parse(
                        chatMessage.createdOn.toLocalDateTime().toString()
                    )!!

                    if(authorID == userId)
                        isSender=true

                    val message = com.mentallys.chatView.model.ChatMessage(
                        chatMessage.id,
                        chatMessage.content.message,
                        chatMessage.senderDisplayName,
                        "",
                        isSender,
                        dataTime,
                        ChatView.TYPE_TEXT//message type
                    )

                    messages.add(message)
                }
            }

        }
        return messages
    }

    override fun createNewChatThreadId(chatClient:ChatClient, userID:String, userName:String, topic:String):String {
        val participantList: MutableList<ChatParticipant> = mutableListOf()

        //the User Info
        val userParticipant = ChatParticipant()
        userParticipant.displayName = userName
        userParticipant.communicationIdentifier = CommunicationUserIdentifier(userID)
        participantList.add(userParticipant)

        val createChatThreadOptions = CreateChatThreadOptions()
        createChatThreadOptions.topic = topic
        createChatThreadOptions.participants = participantList

        val createChatThreadResult: CreateChatThreadResult =
            chatClient.createChatThread(createChatThreadOptions)

        val chatThreadProperties: ChatThreadProperties =
            createChatThreadResult.chatThreadProperties

        return chatThreadProperties.id
    }

    private fun setChatDataFormat(): SimpleDateFormat {
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm")
        formatter.timeZone = UTC
        return formatter
    }

}