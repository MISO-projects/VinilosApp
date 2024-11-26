package com.miso.vinilosapp.data.repositories

import android.app.Application
import android.util.Log
import com.miso.vinilosapp.data.database.daos.SongDao
import com.miso.vinilosapp.data.models.Song
import com.miso.vinilosapp.data.models.SongRequest
import com.miso.vinilosapp.data.repositories.network.NetworkServiceAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class SongRepository(val application: Application, private val songDao: SongDao? = null) {

    private val apiService = NetworkServiceAdapter.apiService

    fun getSongsByAlbumId(albumId: Int): Flow<List<Song>> {
        return flow {
            songDao?.getSongsByAlbumIdFlow(albumId)?.let {
                emit(it)
            }
            try {
                val songsFromApi = apiService.getSongsByAlbumId(albumId)
                songDao?.insertSongs(songsFromApi)
                emit(songsFromApi)
            } catch (e: Exception) {
                Log.e("Repository", "Error syncing data", e)
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun addSongToAlbum(albumId: Int, name: String, duration: String): Song? {
        return withContext(Dispatchers.IO) {
            try {
                val songRequest = SongRequest(name, duration)
                apiService.addSongToAlbum(albumId, songRequest)
            } catch (e: Exception) {
                Log.e(
                    "NetworkError",
                    "Error al agregar la canción al álbum con ID $albumId: ${e.message}",
                    e
                )
                null
            }
        }
    }
}
