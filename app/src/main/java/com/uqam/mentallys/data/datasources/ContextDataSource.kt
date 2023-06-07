package com.uqam.mentallys.data.datasources

import javax.inject.Inject

class ContextDataSource @Inject constructor() {
    fun loadContexts(): List<String> {
        return listOf(
            "Autosoins", "Soutien et accompagnement", "Suivi médical", "Autre (Saisie libre)", "Je ne sais pas"
        )
    }
}
