package com.miso.vinilosapp.data.repositories

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import com.miso.vinilosapp.data.database.daos.ArtistDao
import com.miso.vinilosapp.data.models.Artist
import com.miso.vinilosapp.data.repositories.network.NetworkServiceAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ArtistRepository(val application: Application, private val artistDao: ArtistDao) {

    private val apiService = NetworkServiceAdapter.apiService

    suspend fun getArtists(): List<Artist> {
        return withContext(Dispatchers.IO) {
            return@withContext artistDao.getAllArtists().ifEmpty {
                val cm =
                    application.baseContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                if (cm.activeNetworkInfo?.type != ConnectivityManager.TYPE_WIFI && cm.activeNetworkInfo?.type != ConnectivityManager.TYPE_MOBILE) {
                    emptyList()
                } else {
                    apiService.getArtists()
                }
            }
        }
    }

    suspend fun getArtistById(id: Int): Artist {
        return withContext(Dispatchers.IO) {
            apiService.getArtistById(id)
        }
    }
}
