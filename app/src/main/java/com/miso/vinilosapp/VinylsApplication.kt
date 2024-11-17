package com.miso.vinilosapp

import android.app.Application
import com.miso.vinilosapp.data.database.VinylRoomDatabase

class VinylsApplication : Application() {
    val database by lazy { VinylRoomDatabase.getDatabase(this) }
}
