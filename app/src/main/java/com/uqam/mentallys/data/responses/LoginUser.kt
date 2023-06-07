package com.uqam.mentallys.data.responses

data class LoginUser(
    val email: String,
    val password: String
)

data class ForgetPassword(
    val email: String
)

