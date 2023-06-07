package com.uqam.mentallys.repository

import com.uqam.mentallys.data.dao.ResourceDatabaseDao
import com.uqam.mentallys.data.datasources.ResourceDataSource
import com.uqam.mentallys.model.ChatResourceLoc
import com.uqam.mentallys.model.resource.HistoryEntry
import com.uqam.mentallys.model.resource.Resource
import javax.inject.Inject

class ResourceRepository @Inject constructor(
    private val resourceDatabaseDao: ResourceDatabaseDao,
    private val resourceDataSource: ResourceDataSource,
) : ResourceRepositoryInterface {

    override suspend fun fetch(): List<Resource> {
        return resourceDataSource.load()
    }

    override suspend fun getHistory(): List<HistoryEntry> {
        return resourceDatabaseDao.getHistory()
    }

    override suspend fun saveInHistory(resource: HistoryEntry) {
        resourceDatabaseDao.addHistoryEntry(resource)
        resourceDatabaseDao.sweepHistory()
    }

    override suspend fun clearHistory() {
        resourceDatabaseDao.deleteHistory()
    }

    override suspend fun deleteFromHistory(resource: HistoryEntry) {
        resourceDatabaseDao.deleteHistoryEntry(resource)
    }


}