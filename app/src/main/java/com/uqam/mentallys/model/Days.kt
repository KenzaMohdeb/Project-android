package com.uqam.mentallys.model

import com.uqam.mentallys.R

enum class Days(private val textId : Int) {
    MONDAY(R.string.days_monday),
    TUESDAY(R.string.days_tuesday),
    WEDNESDAY(R.string.days_wednesday),
    THURSDAY(R.string.days_thursday),
    FRIDAY(R.string.days_friday),
    SATURDAY(R.string.days_saturday),
    SUNDAY(R.string.days_sunday);

    fun getTextId() : Int{
        return textId
    }
}