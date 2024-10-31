package com.miso.vinilosapp.data.repositories.network

import com.miso.vinilosapp.data.models.Album
import retrofit2.http.GET

interface ApiService {

    @GET("albums")
    suspend fun getAlbums(): List<Album>

    @GET("albums/{id}")
    suspend fun getAlbumById(id: Int): Album

}
