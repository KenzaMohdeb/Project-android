package com.uqam.mentallys.data.responses

data class LoggedUser(
    val createdAt: String,
    val createdBy: String,
    val deletedAt: String,
    val deletedBy: String,
    val description: String,
    val email: String,
    val firstname: String,
    val fullName: String,
    val image: String,
    val id: String,
    val invitedBy: String,
    val isLocked: Boolean,
    val lastname: String,
    val profileImageUrl: String,
    val roleName: String,
    val updatedAt: String,
    val updatedBy: String
)