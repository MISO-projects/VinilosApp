package com.miso.vinilosapp.data.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.miso.vinilosapp.data.models.Song

@Dao
interface SongDao {
    @Query("SELECT * FROM songs WHERE album_id = :albumId")
    fun getSongsByAlbumIdFlow(albumId: Int): List<Song>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSongs(songs: List<Song>)
}
