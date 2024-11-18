package com.miso.vinilosapp.data.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.miso.vinilosapp.data.models.Artist

@Dao
interface ArtistDao {
    @Query("SELECT * FROM artists")
    fun getAllArtists(): List<Artist>

    @Query("SELECT * FROM artists WHERE artist_id = :artistId")
    fun getArtistById(artistId: Int): Artist

    @Insert
    fun insertAll(vararg artists: Artist)
}
