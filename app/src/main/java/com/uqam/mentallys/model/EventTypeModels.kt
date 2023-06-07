package com.uqam.mentallys.model

data class EventTypeModels(
    val maintenance: String = "Entretien",
    val consultation: String = "Consultation",
    val psychotherapy: String = "Psychothérapie",
    val Hospitalization: String = "Hospitalisation",
    val CommunitySupport: String = "Soutien communautaire",
    val Accommodation: String = "Hébergement",
    val Other: String = "Autre"
)
