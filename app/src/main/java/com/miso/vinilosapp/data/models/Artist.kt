package com.miso.vinilosapp.data.models

import com.google.gson.annotations.SerializedName

data class Artist(
    @SerializedName("id")
    val artistId: Int,
    val name: String,
    val image: String,
    val description: String,
    var birthDate: String,
    val albums: List<Album>
)
