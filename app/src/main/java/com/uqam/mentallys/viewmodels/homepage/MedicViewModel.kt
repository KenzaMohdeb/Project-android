package com.uqam.mentallys.viewmodels.homepage

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.uqam.mentallys.model.Medic
import com.uqam.mentallys.repository.HomePageRepositoryInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MedicViewModel @Inject constructor(private val homePageRepository: HomePageRepositoryInterface) :
    ViewModel() {

    val list = MutableLiveData<List<Medic>>()

    fun fetchMedic() {
        val mockData = homePageRepository.fetchMedic()
        list.value = mockData
    }

}