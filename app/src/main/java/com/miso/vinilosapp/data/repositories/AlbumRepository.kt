package com.miso.vinilosapp.data.repositories

import android.util.Log
import com.miso.vinilosapp.data.cache.AlbumsCacheManager
import com.miso.vinilosapp.data.database.daos.AlbumDao
import com.miso.vinilosapp.data.models.Album
import com.miso.vinilosapp.data.models.AlbumRequest
import com.miso.vinilosapp.data.repositories.network.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class AlbumRepository(
    private val albumsCacheManager: AlbumsCacheManager,
    private val apiService: ApiService,
    private val albumDao: AlbumDao? = null
) {

    suspend fun getAlbums(): Flow<List<Album>> {
        return flow {
            albumDao?.getAllAlbums()?.let { emit(it) }
            try {
                val albumsFromApi = apiService.getAlbums()
                albumDao?.insertAll(albumsFromApi)
                emit(albumsFromApi)
            } catch (e: Exception) {
                Log.e("Repository", "Error syncing data", e)
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getAlbumById(id: Int): Album = withContext(Dispatchers.IO) {
        albumsCacheManager.getAlbumById(id) ?: try {
            apiService.getAlbumById(id)
        } catch (e: Exception) {
            throw RuntimeException("Error fetching album with ID $id from network", e)
        }
    }

    suspend fun addAlbum(album: AlbumRequest): Album = withContext(Dispatchers.IO) {
        try {
            apiService.addAlbum(album)
        } catch (e: Exception) {
            throw RuntimeException("Error adding album to network", e)
        }
    }
}
