package com.uqam.mentallys.data.responses

data class TokenResponse(
    val access_token: String?,
    val refresh_token: String?
)