package com.miso.vinilosapp.network.repositories

import com.miso.vinilosapp.models.Album
import com.miso.vinilosapp.network.NetworkServiceAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AlbumRepository {

    private val apiService = NetworkServiceAdapter.apiService

    suspend fun getAlbums(): List<Album> {
        return withContext(Dispatchers.IO) {
            apiService.getAlbums()
        }
    }

    suspend fun getAlbum(): Album {
        return withContext(Dispatchers.IO) {
            apiService.getAlbum()
        }
    }
}