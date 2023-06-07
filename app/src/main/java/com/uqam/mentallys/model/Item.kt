package com.uqam.mentallys.model

data class Item(
    val createdAt: String,
    val createdBy: Any,
    val deletedAt: Any,
    val deletedBy: Any,
    val description: Any,
    val email: String,
    val firstname: String,
    val fullName: String,
    val id: String,
    val invitedBy: Any,
    val isLocked: Boolean,
    val lastname: String,
    val profileImageUrl: String,
    val roleName: String,
    val updatedAt: String,
    val updatedBy: Any
)