package com.uqam.mentallys.data.datasources

import com.uqam.mentallys.model.Tag
import javax.inject.Inject

class TagDataSource @Inject constructor(){
    fun loadTagsEntretien(): List<Tag> {
        return listOf(
//            "En personne", "Par téléphone", "En vidéo", "Clavardage / Messagerie", "Courriel", "Autre"
            Tag(name = "Infirmière"),
            Tag(name = "Psychoéducateur"),
            Tag(name = "Travailleur Social"),
            Tag(name = "Paire-aidant"),
            Tag(name = "Ergothérapeute"),
            Tag(name = "Autre")
        )
    }
    fun loadTagsConsultation(): List<Tag> {
        return listOf(
            Tag(name = "Médecin de famille"),
            Tag(name = "Psychiatre"),
            Tag(name = "Infirmière"),
            Tag(name = "Psychologue"),
            Tag(name = "Travailleur social"),
            Tag(name = "Psychoéducateur"),
            Tag(name = "Sexologue"),
            Tag(name = "Ergothérapeute"),
            Tag(name = "Coach de vie"),
            Tag(name = "Autre (saisie)"),
        )
    }
    fun loadTagsPsychotherapy(): List<Tag> {
        return listOf(
            Tag(name = "Psychologue"),
            Tag(name = "Psychothérapeute"),
            Tag(name = "Psychiatre"),
            Tag(name = "Travailleur social"),
            Tag(name = "Psychoéducateur"),
            Tag(name = "Sexologue"),
            Tag(name = "Autre (saisie)"),
        )
    }
}