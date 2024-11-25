package com.miso.vinilosapp.data.repositories

import com.miso.vinilosapp.data.cache.AlbumsCacheManager
import com.miso.vinilosapp.data.models.Album
import com.miso.vinilosapp.data.repositories.network.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AlbumRepository(
    private val albumsCacheManager: AlbumsCacheManager,
    private val apiService: ApiService
) {

    suspend fun getAlbums(): List<Album> = withContext(Dispatchers.IO) {
        albumsCacheManager.getAlbums().ifEmpty {
            try {
                val albumsFromApi = apiService.getAlbums()
                albumsCacheManager.saveAlbums(albumsFromApi)
                albumsFromApi
            } catch (e: Exception) {
                throw RuntimeException("Error fetching albums from network", e)
            }
        }
    }

    suspend fun getAlbumsByCollectorId(collectorId: Int): List<Album> = withContext(Dispatchers.IO) {
        try {
            val albumsFromApi = apiService.getAlbumsByCollectorId(collectorId)
            albumsFromApi.map { it.album }
        } catch (e: Exception) {
            throw RuntimeException("Error fetching albums by collector from network", e)
        }
    }

    suspend fun getAlbumById(id: Int): Album = withContext(Dispatchers.IO) {
        albumsCacheManager.getAlbumById(id) ?: try {
            apiService.getAlbumById(id)
        } catch (e: Exception) {
            throw RuntimeException("Error fetching album with ID $id from network", e)
        }
    }
}
