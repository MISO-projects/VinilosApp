package com.miso.vinilosapp.data.repositories

import com.miso.vinilosapp.data.models.Artist
import com.miso.vinilosapp.data.repositories.network.NetworkServiceAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ArtistRepository {

    private val apiService = NetworkServiceAdapter.apiService

    suspend fun getArtists(): List<Artist> {
        return withContext(Dispatchers.IO) {
            apiService.getArtists()
        }
    }

    suspend fun getArtistById(id: Int): Artist {
        return withContext(Dispatchers.IO) {
            apiService.getArtistById(id)
        }
    }
}
