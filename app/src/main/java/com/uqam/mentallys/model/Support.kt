package com.uqam.mentallys.model

import java.util.*

class Support(
    val id: UUID = UUID.randomUUID(),
    val title: String,
    val icon: Int
) {
    override fun equals(other: Any?): Boolean =
        (other is Medic) && hashCode() == other.hashCode()

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + title.hashCode()
        return result
    }
}