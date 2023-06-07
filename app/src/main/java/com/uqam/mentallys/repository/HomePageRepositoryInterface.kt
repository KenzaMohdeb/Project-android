package com.uqam.mentallys.repository

import com.uqam.mentallys.model.Medic
import com.uqam.mentallys.model.Private
import com.uqam.mentallys.model.Suggestion
import com.uqam.mentallys.model.Support

interface HomePageRepositoryInterface {
    fun fetchMedic(): List<Medic>
    fun fetchPrivate(): List<Private>
    fun fetchSupport(): List<Support>
    fun fetchSuggestion(): List<Suggestion>
}