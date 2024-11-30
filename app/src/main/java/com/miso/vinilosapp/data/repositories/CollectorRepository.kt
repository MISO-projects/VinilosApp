package com.miso.vinilosapp.data.repositories

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import com.miso.vinilosapp.data.database.daos.CollectorDao
import com.miso.vinilosapp.data.models.Collector
import com.miso.vinilosapp.data.repositories.network.NetworkServiceAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CollectorRepository(
    val application: Application,
    private val collectorDao: CollectorDao
) {

    private val apiService = NetworkServiceAdapter.apiService

    suspend fun getCollectors(): List<Collector> {
        return withContext(Dispatchers.IO) {
            return@withContext collectorDao.getAllCollectors().ifEmpty {
                val cm =
                    application.baseContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                if (cm.activeNetworkInfo?.type != ConnectivityManager.TYPE_WIFI && cm.activeNetworkInfo?.type != ConnectivityManager.TYPE_MOBILE) {
                    emptyList()
                } else {
                    apiService.getCollectors()
                }
            }
        }
    }

    suspend fun getCollectorById(id: Int): Collector {
        return withContext(Dispatchers.IO) {
            apiService.getCollectorById(id)
        }
    }
}
