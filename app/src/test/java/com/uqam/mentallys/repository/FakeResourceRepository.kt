package com.uqam.mentallys.repository

import com.uqam.mentallys.data.datasource.FakeResourceDatasource
import com.uqam.mentallys.model.resource.HistoryEntry
import com.uqam.mentallys.model.resource.Resource

class FakeResourceRepository : ResourceRepositoryInterface {
    override suspend fun fetch(): List<Resource> {
        return FakeResourceDatasource().load()
    }

    override suspend fun saveInHistory(resource: HistoryEntry) {
        TODO("Not yet implemented")
    }

    override suspend fun clearHistory() {
        TODO("Not yet implemented")
    }

    override suspend fun deleteFromHistory(resource: HistoryEntry) {
        TODO("Not yet implemented")
    }

    override suspend fun getHistory(): List<HistoryEntry> {
        TODO("Not yet implemented")
    }
}