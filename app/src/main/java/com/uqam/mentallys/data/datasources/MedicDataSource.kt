package com.uqam.mentallys.data.datasources

import com.uqam.mentallys.R
import com.uqam.mentallys.model.Medic
import javax.inject.Inject

class MedicDataSource @Inject constructor() {

    fun loadMedics(): List<Medic> {
        return listOf(
            Medic(title = "Aujourd’hui ou demain", icon = R.drawable.ic_baseline_access_time_24),
            Medic(
                title = "Super-clinique 7 jours sur 7",
                icon = R.drawable.ic_baseline_stethoscope_24
            ),
            Medic(title = "Clinique universitaire", icon = R.drawable.ic_baseline_hospital_24)
        )
    }
}
