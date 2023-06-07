package com.uqam.mentallys.view.ui.message

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uqam.mentallys.model.ChatParticipantInfo
import com.uqam.mentallys.model.ChatResourceLoc
import com.uqam.mentallys.repository.ChatRepositoryInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedChatUserInfo @Inject constructor(
    private val repository: ChatRepositoryInterface
) : ViewModel() {

    val list = MutableLiveData<SelectedUserInfo>()

    private val _chatParticipantInfo: MutableLiveData<ChatResourceLoc> = MutableLiveData()
    val chatParticipantInfo: LiveData<ChatResourceLoc>
        get() = _chatParticipantInfo

    private val _participantListVisible: MutableLiveData<Boolean> = MutableLiveData()
    val participantListVisible: LiveData<Boolean>
        get() = _participantListVisible

    fun setSharedObject(selectedUserInfo: SelectedUserInfo) {
        list.value = selectedUserInfo
    }

    fun showParticipantList(visible: Boolean):Boolean? {
        _participantListVisible.value = visible
        return _participantListVisible.value
    }
    fun getUserInfo(): SelectedUserInfo? {
       return list.value
    }

    fun setChatParticipantInfo(id:String) = viewModelScope.launch {
        _chatParticipantInfo.value = repository.getResourceByCommunicationUserId(id)
    }

}