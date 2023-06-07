package com.uqam.mentallys.model.resource

import androidx.annotation.Keep
import com.uqam.mentallys.R
import com.uqam.mentallys.model.interfaces.ConvertibleToCard

/*
 * Contains all resources client and linked text resources
 */
@Keep
enum class Client(private val textId : Int) : ConvertibleToCard {
    TEENAGER(R.string.resource_teenager),
    ADULT(R.string.resource_adult),
    FAMILY(R.string.resource_family),
    ELDER(R.string.resource_elder);

    override fun getTextId() : Int {
        return this.textId
    }

    override fun getImageId() : Int? {
        return null
    }
}