package com.miso.vinilosapp.data.dto

import com.miso.vinilosapp.data.models.Album
import com.miso.vinilosapp.data.models.Collector

data class CollectorAlbumResponse(
    val id: Int,
    val price: Double,
    val status: String,
    val album: Album,
    val collector: Collector
)