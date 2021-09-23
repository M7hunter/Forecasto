package com.m7.forecasto.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.m7.forecasto.data.model.Weather

@Dao
interface WeatherDao {

    @Insert(onConflict = REPLACE)
    suspend fun saveWeatherList(weatherList: List<Weather>)

    @Query("SELECT * FROM Weather WHERE cityId LIKE :cityId")
    suspend fun getWeatherListByCityId(cityId: Int): List<Weather>
}