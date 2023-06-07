package com.uqam.mentallys.data.datasources

import com.uqam.mentallys.R
import com.uqam.mentallys.model.EventType
import javax.inject.Inject

class EventTypeDataSource @Inject constructor(){

    fun loadEventTypes(): List<EventType> {
        return listOf(
            EventType(typeName = "Entretien", icon = R.drawable.entretien),
            EventType(typeName = "Consultation", icon = R.drawable.consultation),
            EventType(typeName = "Psychothérapie", icon = R.drawable.psychotherapy),
            EventType(typeName = "Hospitalisation", icon = R.drawable.ic_baseline_hospital_24),
            EventType(
                typeName = "Soutien communautaire",
                icon = R.drawable.ic_baseline_hospital_24
            ),
            EventType(typeName = "Hébergement", icon = R.drawable.home),
            EventType(typeName = "Autre", icon = R.drawable.other),
        )
    }
}