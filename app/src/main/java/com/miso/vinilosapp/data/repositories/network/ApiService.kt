package com.miso.vinilosapp.data.repositories.network

import com.miso.vinilosapp.data.dto.CollectorAlbumResponse
import com.miso.vinilosapp.data.models.Album
import com.miso.vinilosapp.data.models.Artist
import com.miso.vinilosapp.data.models.Collector
import com.miso.vinilosapp.data.models.Song
import com.miso.vinilosapp.data.models.SongRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    @GET("albums")
    suspend fun getAlbums(): List<Album>

    @GET("albums/{id}")
    suspend fun getAlbumById(@Path("id") id: Int): Album

    @GET("albums/{id}/tracks")
    suspend fun getSongsByAlbumId(@Path("id") id: Int): List<Song>

    @POST("albums/{id}/tracks")
    suspend fun addSongToAlbum(@Path("id") id: Int, @Body songRequest: SongRequest): Song

    @GET("musicians")
    suspend fun getArtists(): List<Artist>

    @GET("musicians/{id}")
    suspend fun getArtistById(@Path("id") id: Int): Artist

    @GET("collectors")
    suspend fun getCollectors(): List<Collector>

    @GET("collectors/{id}")
    suspend fun getCollectorById(@Path("id") id: Int): Collector

    @GET("collectors/{id}/albums")
    suspend fun getAlbumsByCollectorId(@Path("id") id: Int): List<CollectorAlbumResponse>
}
