package com.miso.vinilosapp.data.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.miso.vinilosapp.data.models.Album

class AlbumListConverter {
    @TypeConverter
    fun fromAlbumList(albums: List<Album>): String {
        val gson = Gson()
        val type = object : TypeToken<List<Album>>() {}.type
        return gson.toJson(albums, type)
    }

    @TypeConverter
    fun toAlbumList(albumsString: String): List<Album> {
        val gson = Gson()
        val type = object : TypeToken<List<Album>>() {}.type
        return gson.fromJson(albumsString, type)
    }
}
