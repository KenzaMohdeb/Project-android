package com.uqam.mentallys.data.responses

data class LoginUserInfo(
    val id: String?,
    val email: String?,
    val firstName: String?,
    val lastName: String?,
    val fullName: String?,
    val image: String?,
    val CommunicationUserId:String?,
    val AccessToken:String?,
    val AccessTokenExpiresOn:String?,
)