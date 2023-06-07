package com.uqam.mentallys.model.resource

import androidx.annotation.Keep
import com.uqam.mentallys.R
import com.uqam.mentallys.model.interfaces.ConvertibleToCard

/*
 * Contains all resources categories and linked text resources
 */
@Keep
enum class Category(private val textId: Int,private val textIdPlural : Int,  private val iconId: Int) : ConvertibleToCard {
    PUBLIC(R.string.resource_public,R.string.resource_public_plural, R.drawable.ic_baseline_hospital_24),
    COMMUNITY(R.string.resource_community,R.string.resource_community_plural, R.drawable.ic_baseline_people_24),
    PRIVATE(R.string.resource_private,R.string.resource_private_plural, R.drawable.ic_baseline_couch_24);

    override fun getTextId() : Int {
        return this.textId
    }

    fun getTextIdPlural() : Int {
        return this.textIdPlural
    }

    override fun getImageId() : Int {
        return this.iconId
    }
}