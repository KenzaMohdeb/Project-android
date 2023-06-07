package com.uqam.mentallys.model

import java.util.*

data class Suggestion(
    val id: UUID = UUID.randomUUID(),
    val description: String,
)
