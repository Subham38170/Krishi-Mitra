package com.example.krishimitra.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.krishimitra.data.local.dao.MandiPriceDao
import com.example.krishimitra.data.local.dao.WeatherDao
import com.example.krishimitra.data.local.entity.MandiPriceEntity
import com.example.krishimitra.data.local.entity.WeatherEntity
import com.example.krishimitra.data.local.type_converters.HourlyListConverter


@Database(
    entities = [MandiPriceEntity::class, WeatherEntity::class],
    version = 1
)
@TypeConverters(HourlyListConverter::class)
abstract class KrishiMitraDatabase: RoomDatabase(){

    abstract fun mandiPriceDao(): MandiPriceDao


    abstract fun weatherDataDao(): WeatherDao
}