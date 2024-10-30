package com.miso.vinilosapp.network

import com.miso.vinilosapp.models.Album
import retrofit2.http.GET

interface ApiService {
    @GET("albums")
    suspend fun getAlbums(): List<Album>
}
