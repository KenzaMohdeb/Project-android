package com.uqam.mentallys.model.resource

import androidx.annotation.Keep
import com.uqam.mentallys.R
import com.uqam.mentallys.model.interfaces.ConvertibleToCard

/*
 * Contains all resources activities and linked text resources
 */
@Keep
enum class Activity(private val textId: Int) : ConvertibleToCard {
    SOCIAL_WORKER(R.string.resource_social_worker),
    PSYCHOLOGIST(R.string.resource_psychologist),
    FAMILY_THERAPIST(R.string.resource_family_therapist),
    SEXOLOGIST(R.string.resource_sexologist),
    OCCUPATIONAL_THERAPIST(R.string.resource_occupational_therapist),
    PEER(R.string.resource_peer),
    PSYCHOEDUCATOR(R.string.resource_psychoeducator),
    PSYCHIATRIST(R.string.resource_psychiatrist),
    NURSE(R.string.resource_nurse),
    FAMILY_DOCTOR(R.string.resource_family_doctor);

    override fun getTextId() : Int {
        return this.textId
    }

    override fun getImageId() : Int? {
        return null
    }
}