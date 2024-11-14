package com.miso.vinilosapp.data.cache

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.miso.vinilosapp.data.models.Album

class AlbumsCacheManager(private val context: Context) {

    private val ALBUMS_CACHE = "albums_cache"
    private val gson = Gson()

    fun clearAlbums() {
        val sharedPreferences = context.getSharedPreferences(ALBUMS_CACHE, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }

    fun saveAlbums(albums: List<Album>) {
        val sharedPreferences = context.getSharedPreferences(ALBUMS_CACHE, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val albumsJson = gson.toJson(albums)
        editor.putString(ALBUMS_CACHE, albumsJson)
        editor.apply()
    }

    fun getAlbums(): List<Album> {
        val sharedPreferences = context.getSharedPreferences(ALBUMS_CACHE, Context.MODE_PRIVATE)
        val albumsJson = sharedPreferences.getString(ALBUMS_CACHE, null)

        return if (albumsJson != null) {
            val type = object : TypeToken<List<Album>>() {}.type
            gson.fromJson(albumsJson, type)
        } else {
            emptyList()
        }
    }

    fun getAlbumById(albumId: Int): Album? {
        val albums = getAlbums()
        return albums.find { it.albumId == albumId }
    }
}
