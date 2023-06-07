package com.uqam.mentallys.data.responses

data class CurrentUser(
    val createdAt: String,
    val createdBy: Any,
    val deletedAt: Any,
    val deletedBy: Any,
    val description: Any,
    val email: String,
    val firstname: String,
    val fullName: String,
    val image: String,
    val id: String,
    val invitedBy: Any,
    val isLocked: Boolean,
    val lastname: String,
    val profileImageUrl: Any,
    val roleName: String,
    val updatedAt: String,
    val updatedBy: Any,
    val communicationUserId: String,
    val accessToken: String,
    val accessTokenExpiresOn: String
)