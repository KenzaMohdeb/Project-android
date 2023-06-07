package com.uqam.mentallys.data.datasources

import com.uqam.mentallys.model.Suggestion
import javax.inject.Inject

class SuggestionDataSource @Inject constructor() {

    fun loadSuggestion(): List<Suggestion> {
        return listOf(
            Suggestion(description = "Séparation"),
            Suggestion(description = "Alcool"),
            Suggestion(description = "Travail"),
            Suggestion(description = "Test2"),
            Suggestion(description = "Test3"),
        )
    }
}
