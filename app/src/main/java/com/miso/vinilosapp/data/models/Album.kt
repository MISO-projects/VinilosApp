package com.miso.vinilosapp.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "albums")
data class Album(
    @PrimaryKey
    @SerializedName("id")
    @ColumnInfo(name = "album_id")
    val albumId: Int,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "cover")
    val cover: String,

    @ColumnInfo(name = "release_date")
    val releaseDate: String,

    @ColumnInfo(name = "description")
    val description: String,

    @ColumnInfo(name = "genre")
    val genre: String,

    @ColumnInfo(name = "record_label")
    val recordLabel: String,

    @ColumnInfo(name = "tracks")
    val tracks: List<Song>
)
