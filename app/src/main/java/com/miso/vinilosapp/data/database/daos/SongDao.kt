package com.miso.vinilosapp.data.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.miso.vinilosapp.data.models.Song

@Dao
interface SongDao {
    @Query("SELECT * FROM songs")
    fun getAllSongs(): List<Song>

    @Query("SELECT * FROM songs WHERE song_id = :songId")
    fun getSongById(songId: Int): Song

    @Insert
    fun insertAll(vararg songs: Song)
}
