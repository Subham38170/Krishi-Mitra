package com.example.krishimitra.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.krishimitra.data.local.dao.MandiPriceDao
import com.example.krishimitra.data.local.entity.MandiPriceEntity


@Database(
    entities = [MandiPriceEntity::class],
    version = 1
)
abstract class KrishiMitraDatabase: RoomDatabase(){

    abstract fun mandiPriceDao(): MandiPriceDao
}