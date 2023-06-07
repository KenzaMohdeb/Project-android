package com.uqam.mentallys.model.resource

import androidx.annotation.Keep
import com.uqam.mentallys.R
import com.uqam.mentallys.model.interfaces.ConvertibleToCard

/*
 * Contains all resources cost and linked text resources
 */
@Keep
enum class Cost(private val textId : Int) : ConvertibleToCard {
    FREE(R.string.resource_free),
    REDUCED(R.string.resource_reduce),
    PAID(R.string.resource_paid);

    override fun getTextId() : Int {
        return this.textId
    }

    override fun getImageId() : Int? {
        return null
    }
}