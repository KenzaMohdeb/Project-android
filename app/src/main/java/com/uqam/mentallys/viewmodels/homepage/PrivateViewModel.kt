package com.uqam.mentallys.viewmodels.homepage

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.uqam.mentallys.model.Private
import com.uqam.mentallys.repository.HomePageRepositoryInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PrivateViewModel @Inject constructor(private val homePageRepository: HomePageRepositoryInterface) :
    ViewModel() {

    val list = MutableLiveData<List<Private>>()

    fun fetchPrivate() {
        val mockData = homePageRepository.fetchPrivate()
        list.value = mockData
    }

}