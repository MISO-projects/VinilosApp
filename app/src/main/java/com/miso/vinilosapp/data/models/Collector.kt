package com.miso.vinilosapp.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.miso.vinilosapp.data.database.AlbumListConverter

@Entity(tableName = "collectors")
data class Collector(
    @PrimaryKey
    @ColumnInfo(name = "collector_id")
    val collectorId: Int,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "telephone")
    val telephone: String,

    @ColumnInfo(name = "email")
    val email: String,

    @TypeConverters(AlbumListConverter::class)
    @ColumnInfo(name = "collector_albums")
    val collectorAlbums: List<Album>
) {
    fun getInitials(): String {
        return name.split(" ").mapNotNull { it.firstOrNull()?.toString() }.joinToString("")
    }
}
