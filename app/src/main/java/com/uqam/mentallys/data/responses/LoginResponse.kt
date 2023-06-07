package com.uqam.mentallys.data.responses

data class LoginResponse(
    val currentUser: CurrentUser,
    val token: String
)