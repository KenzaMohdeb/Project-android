package com.uqam.mentallys.repository

import com.uqam.mentallys.model.ChatResourceLoc
import com.uqam.mentallys.model.resource.HistoryEntry
import com.uqam.mentallys.model.resource.Resource

interface ResourceRepositoryInterface {
    suspend fun fetch(): List<Resource>
    suspend fun saveInHistory(resource: HistoryEntry)
    suspend fun clearHistory()
    suspend fun deleteFromHistory(resource: HistoryEntry)
    suspend fun getHistory(): List<HistoryEntry>
}