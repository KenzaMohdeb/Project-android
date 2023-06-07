package com.uqam.mentallys.data.responses

data class ResponseLogin(
    val currentUser: CurrentUser,
    val token: String
)