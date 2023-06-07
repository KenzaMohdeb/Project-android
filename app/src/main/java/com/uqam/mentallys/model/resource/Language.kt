package com.uqam.mentallys.model.resource

import androidx.annotation.Keep
import com.uqam.mentallys.R
import com.uqam.mentallys.model.interfaces.ConvertibleToCard

/*
 * Contains all resources languages and linked text resources
 */
@Keep
enum class Language(private val textId : Int) : ConvertibleToCard {
    FRENCH(R.string.language_french),
    ENGLISH(R.string.language_english),
    SPANISH(R.string.language_spanish),
    GERMAN(R.string.language_german);

    override fun getTextId() : Int {
        return this.textId
    }

    override fun getImageId() : Int? {
        return null
    }
}