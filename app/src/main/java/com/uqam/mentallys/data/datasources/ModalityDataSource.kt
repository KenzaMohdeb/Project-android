package com.uqam.mentallys.data.datasources

import javax.inject.Inject

class ModalityDataSource @Inject constructor() {
    fun loadMaintenanceModalities(): List<String> {
        return listOf(
            "En personne", "Par téléphone", "En vidéo", "Clavardage / Messagerie", "Courriel", "Autre")
    }

    fun loadConsultationModalities(): List<String> {
        return listOf(
            "En personne", "Par téléphone", "En vidéo", "Autre (saisie)")
    }

    fun loadPsychotherapyModalities(): List<String> {
        return listOf(
            "En personne", "À distance", "Mixte", "Autre (saisie)")
    }

    fun loadCommunitySupportModalities(): List<String> {
        return listOf(
            "En personne", "À distance", "Mixte", "Autre (saisie)")
    }

    fun loadCommunitySupportOptions(): List<String> {
        return listOf(
            "Séance d'information", "Suivi individuel ou familial", "Groupe de soutien", "Atelier", "Formation", "Autre (saisie)")
    }
}