package com.uqam.mentallys.model

data class ChatResourcesNet(
    val hasNext: Boolean,
    val items: List<ChatResource>,
    val totalCount: Int

)