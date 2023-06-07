package com.uqam.mentallys.view.ui.message

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.uqam.mentallys.model.ChatResourceLoc
import com.uqam.mentallys.repository.ChatRepositoryInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class IntervenantViewModel @Inject constructor(
    private val repository: ChatRepositoryInterface
) : ViewModel() {
    val resource = MutableLiveData<List<ChatResourceLoc>>()
    val intervenantProfile = MutableLiveData<ChatResourceLoc>()

    /**********to fetch the Data from the API Server ***/
//val items: List<ChatResource>,
   /* private val _fetchResourcesResponse: MutableLiveData<Resource<ChatResourceLoc>> = MutableLiveData()
    val fetchResourcesResponse: LiveData<Resource<ChatResourceLoc>>
        get() = _fetchResourcesResponse*/

  /*  fun fetchResources(
        roleName:String
    ) = viewModelScope.launch {
        var result = userRepository.getChatResources(roleName)
        _fetchResourcesResponse.value = result
    }*/

    /**************the use of the static Data***/
     fun fetchResourcesLocal() {
      resource.value = repository.fetch()
     }
    fun fetchResourceByEmail(id:String) {
        intervenantProfile.value = repository.getResourceByEmail(id)
    }
}