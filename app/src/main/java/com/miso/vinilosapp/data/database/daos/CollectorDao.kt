package com.miso.vinilosapp.data.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.miso.vinilosapp.data.models.Collector

@Dao
interface CollectorDao {
    @Query("SELECT * FROM collectors")
    fun getAllCollectors(): List<Collector>

    @Query("SELECT * FROM collectors WHERE collector_id = :collectorId")
    fun getCollectorById(collectorId: Int): Collector

    @Insert
    fun insertAll(vararg collectors: Collector)
}
