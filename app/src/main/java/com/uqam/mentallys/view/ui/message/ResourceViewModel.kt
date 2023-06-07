package com.uqam.mentallys.view.ui.message

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.uqam.mentallys.model.ChatResource
import com.uqam.mentallys.model.ChatResourcesNet
import com.uqam.mentallys.network.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class ResourceViewModel @Inject constructor() : ViewModel() {

    val resource = MutableLiveData<List<ChatResource>>()

    private val _fetchResourcesResponse: MutableLiveData<Resource<ChatResourcesNet>> = MutableLiveData()
    val fetchResourcesResponse: LiveData<Resource<ChatResourcesNet>>
        get() = _fetchResourcesResponse

}