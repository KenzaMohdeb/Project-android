package com.mentallys.chatView.model

import java.util.*

data class ChatMessage(
    var id: String = System.currentTimeMillis().toString(),
    var message: String = "",
    var username: String = "",
    var profile_url: String = "",
    var is_from_me: Boolean = true,
    var createdAt: Date? = null,
    var message_type: Int = 0,
    var view_type: Int? = 0
)