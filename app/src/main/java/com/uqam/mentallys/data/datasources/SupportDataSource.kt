package com.uqam.mentallys.data.datasources

import com.uqam.mentallys.R
import com.uqam.mentallys.model.Support
import javax.inject.Inject

class SupportDataSource @Inject constructor() {

    fun loadSupport(): List<Support> {
        return listOf(
            Support(title = "Travailleur social", icon = R.drawable.ic_baseline_couch_24),
            Support(title = "Psychologue", icon = R.drawable.ic_baseline_couch_24),
            Support(
                title = "Thérapeute conjugal et familial",
                icon = R.drawable.ic_baseline_people_24
            ),
            Support(title = "Sexologue", icon = R.drawable.ic_baseline_couch_24),
            Support(title = "Ergothérapeute", icon = R.drawable.ic_baseline_people_24),
            Support(title = "Psychoéducateur", icon = R.drawable.ic_baseline_people_24),
            Support(title = "Psychiatre", icon = R.drawable.ic_baseline_couch_24),
        )
    }
}
