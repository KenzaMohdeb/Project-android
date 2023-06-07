package com.uqam.mentallys.repository

import com.uqam.mentallys.data.datasources.MedicDataSource
import com.uqam.mentallys.data.datasources.PrivateDataSource
import com.uqam.mentallys.data.datasources.SuggestionDataSource
import com.uqam.mentallys.data.datasources.SupportDataSource
import com.uqam.mentallys.model.Medic
import com.uqam.mentallys.model.Private
import com.uqam.mentallys.model.Suggestion
import com.uqam.mentallys.model.Support
import javax.inject.Inject

class HomePageRepository @Inject constructor(
    private val homePageMedicList: MedicDataSource,
    private val homePageSupportList: SupportDataSource,
    private val homePagePrivateList: PrivateDataSource,
    private val homePageSuggestionList: SuggestionDataSource,
) : HomePageRepositoryInterface {

    override fun fetchMedic(): List<Medic> =
        homePageMedicList.loadMedics()

    override fun fetchPrivate(): List<Private> =
        homePagePrivateList.loadPrivate()

    override fun fetchSupport(): List<Support> =
        homePageSupportList.loadSupport()

    override fun fetchSuggestion(): List<Suggestion> =
        homePageSuggestionList.loadSuggestion()
}