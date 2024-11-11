package com.miso.vinilosapp.data.repositories

import com.miso.vinilosapp.data.models.Collector
import com.miso.vinilosapp.data.repositories.network.NetworkServiceAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CollectorRepository {

    private val apiService = NetworkServiceAdapter.apiService

    suspend fun getCollectors(): List<Collector> {
        return withContext(Dispatchers.IO) {
            apiService.getCollectors()
        }
    }

}