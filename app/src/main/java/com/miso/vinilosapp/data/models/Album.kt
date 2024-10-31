package com.miso.vinilosapp.data.models

import com.google.gson.annotations.SerializedName

data class Album(
    @SerializedName("id")
    val albumId: Int,
    val name: String,
    val cover: String,
    val releaseDate: String,
    val description: String,
    val genre: String,
    val recordLabel: String
)