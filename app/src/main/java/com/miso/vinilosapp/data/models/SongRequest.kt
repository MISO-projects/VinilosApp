package com.miso.vinilosapp.data.models

import androidx.room.ColumnInfo

data class SongRequest(

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "duration")
    val duration: String
)
