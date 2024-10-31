package com.miso.vinilosapp.data.repositories.network

import com.miso.vinilosapp.data.models.Album
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("albums")
    suspend fun getAlbums(): List<Album>

    @GET("albums/{id}")
    suspend fun getAlbumById(@Path("id") id: Int): Album

}
