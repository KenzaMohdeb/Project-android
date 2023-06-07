package com.uqam.mentallys.view.ui.message

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.azure.android.communication.chat.ChatClient
import com.azure.android.communication.chat.ChatThreadClient
import com.uqam.mentallys.data.UserPreferences
import com.uqam.mentallys.data.responses.LoginUserInfo
import com.uqam.mentallys.data.responses.UserDtoItem
import com.uqam.mentallys.model.ChatParticipantInfo
import com.uqam.mentallys.model.ListChatThreadClients
import com.uqam.mentallys.network.Resource
import com.uqam.mentallys.repository.ChatRepositoryInterface
import com.uqam.mentallys.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val chatRepositoryInterface: ChatRepositoryInterface,
    private val userPreferences: UserPreferences
) : ViewModel() {
    private val _chatSessionInfo: MutableLiveData<Resource<UserDtoItem>> = MutableLiveData()
    val chatSessionInfo: LiveData<Resource<UserDtoItem>>
        get() = _chatSessionInfo

    private val _chatMessages: MutableLiveData<List<com.mentallys.chatView.model.ChatMessage>> = MutableLiveData()
    val chatMessages: LiveData<List<com.mentallys.chatView.model.ChatMessage>>
        get() = _chatMessages

    private val _existingChatThreadId: MutableLiveData<List<ChatParticipantInfo>> = MutableLiveData()
    val existingChatThreadId: LiveData<List<ChatParticipantInfo>>
        get() = _existingChatThreadId

    private val _threadId: MutableLiveData<String> = MutableLiveData()
    val threadId: LiveData<String>
        get() = _threadId

    private val _listChatThreadClient: MutableLiveData<ListChatThreadClients> = MutableLiveData()
    val listChatThreadClient: LiveData<ListChatThreadClients>
        get() = _listChatThreadClient


    fun refreshAccessTokenAsync() = viewModelScope.launch {
        val result = userRepository.refreshAccessTokenAsync()
        _chatSessionInfo.value = result

    }

    fun getChatMessages(chatThreadClient:ChatThreadClient, userId:String
    ) = viewModelScope.launch {
        val result = chatRepositoryInterface.getChatMessages(chatThreadClient, userId)
        _chatMessages.value = result
    }

    fun createNewChatThreadId(chatClient:ChatClient, userID:String, userName:String, topic:String)
            = viewModelScope.launch {
        val result = chatRepositoryInterface.createNewChatThreadId(chatClient, userID, userName, topic)
        _threadId.value = result
    }

    fun getListChatThreadClient(chatClient:ChatClient) = viewModelScope.launch {
        val result = chatRepositoryInterface.getListChatThreadClient(chatClient)
        _listChatThreadClient.value = result
    }

    fun getExistingChatRoomThreadId(listChatThreadClients: ListChatThreadClients,
                                    communicationInvitedUserId:String, userId: String) = viewModelScope.launch {
        val result = chatRepositoryInterface.getExistingChatRoomThreadId(listChatThreadClients,communicationInvitedUserId, userId)
        _existingChatThreadId.value = result
    }
    fun getUserInfo(): kotlinx.coroutines.flow.Flow<LoginUserInfo> {
        return userPreferences.getUserInfo()
    }
    fun saveUserInfo(communicationUserId: String, accessToken: String, accessTokenExpiresOn:String )= viewModelScope.launch{
        userPreferences.saveAzureInfo(communicationUserId, accessToken, accessTokenExpiresOn )
    }

}