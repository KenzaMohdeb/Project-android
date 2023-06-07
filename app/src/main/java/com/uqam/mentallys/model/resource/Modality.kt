package com.uqam.mentallys.model.resource

import androidx.annotation.Keep
import com.uqam.mentallys.R
import com.uqam.mentallys.model.interfaces.ConvertibleToCard

/*
 * Contains all resources modalities and linked text resources
 */
@Keep
enum class Modality(private val textId : Int, private val iconId : Int) : ConvertibleToCard {
    ON_SITE(R.string.resource_on_site, R.drawable.ic_baseline_consultation),
    REMOTE(R.string.resource_remote, R.drawable.ic_baseline_computer);

    override fun getTextId() : Int {
        return this.textId
    }

    override fun getImageId() : Int {
        return this.iconId
    }
}