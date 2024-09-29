package com.jefisu.domain.repository

interface DataSyncRepository {
    suspend fun observeDataStoreChanges()
}