package com.uqam.mentallys.model

import java.util.*

data class Tag(
    val id: UUID = UUID.randomUUID(),
    val name: String
)
