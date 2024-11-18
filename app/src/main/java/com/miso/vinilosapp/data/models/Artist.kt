package com.miso.vinilosapp.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "artists")
data class Artist(
    @PrimaryKey
    @SerializedName("id")
    @ColumnInfo(name = "artist_id")
    val artistId: Int,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "image")
    val image: String,

    @ColumnInfo(name = "description")
    val description: String,

    @ColumnInfo(name = "birth_date")
    var birthDate: String,

    @ColumnInfo(name = "albums")
    val albums: List<Album>
)
