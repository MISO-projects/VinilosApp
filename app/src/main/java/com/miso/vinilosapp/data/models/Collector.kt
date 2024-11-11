package com.miso.vinilosapp.data.models

data class Collector(
    val id: Int,
    val name: String,
    val telephone: String,
    val email: String,
    val collectorAlbums: List<Album>
)
