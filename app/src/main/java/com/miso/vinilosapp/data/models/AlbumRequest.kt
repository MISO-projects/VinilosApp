package com.miso.vinilosapp.data.models

import androidx.room.ColumnInfo

data class AlbumRequest(
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
    val recordLabel: String
)
