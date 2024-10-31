package com.miso.vinilosapp.data.repositories

import android.util.Log
import com.miso.vinilosapp.data.models.Album
import com.miso.vinilosapp.data.repositories.network.NetworkServiceAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AlbumRepository {

    private val apiService = NetworkServiceAdapter.apiService

    suspend fun getAlbums(): List<Album> {
        return withContext(Dispatchers.IO) {
            apiService.getAlbums()
        }
    }

    suspend fun getAlbumById(id: Int): Album? {
        return withContext(Dispatchers.IO) {
            try {
                apiService.getAlbumById(id)
            } catch (e: Exception) {
                Log.e("NetworkError", "Error al obtener el álbum con ID $id: ${e.message}", e)
                null
            }
        }
    }
}