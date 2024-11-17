package com.miso.vinilosapp.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "songs")
data class Song(
    @PrimaryKey
    @ColumnInfo(name = "song_id")
    val songId: Int,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "duration")
    val duration: String
)
