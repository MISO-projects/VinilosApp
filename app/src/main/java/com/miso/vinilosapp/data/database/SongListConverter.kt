package com.miso.vinilosapp.data.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.miso.vinilosapp.data.models.Song

class SongListConverter {
    @TypeConverter
    fun fromSongList(songs: List<Song>): String {
        val gson = Gson()
        val type = object : TypeToken<List<Song>>() {}.type
        return gson.toJson(songs, type)
    }

    @TypeConverter
    fun toSongList(songsString: String): List<Song> {
        val gson = Gson()
        val type = object : TypeToken<List<Song>>() {}.type
        return gson.fromJson(songsString, type)
    }
}
