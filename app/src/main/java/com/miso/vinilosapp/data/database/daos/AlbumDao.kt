package com.miso.vinilosapp.data.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.miso.vinilosapp.data.models.Album

@Dao
interface AlbumDao {
    @Query("SELECT * FROM albums")
    fun getAllAlbums(): List<Album>

    @Query("SELECT * FROM albums WHERE album_id = :albumId")
    fun getAlbumById(albumId: Int): Album

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(albums: List<Album>)
}
