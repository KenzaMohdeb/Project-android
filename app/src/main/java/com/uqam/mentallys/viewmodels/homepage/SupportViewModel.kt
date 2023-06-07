package com.uqam.mentallys.viewmodels.homepage

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.uqam.mentallys.model.Support
import com.uqam.mentallys.repository.HomePageRepositoryInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SupportViewModel @Inject constructor(private val homePageRepository: HomePageRepositoryInterface) :
    ViewModel() {

    val list = MutableLiveData<List<Support>>()

    fun fetchSupport() {
        val mockData = homePageRepository.fetchSupport()
        list.value = mockData
    }

}