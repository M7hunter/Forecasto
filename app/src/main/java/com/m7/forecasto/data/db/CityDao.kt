package com.m7.forecasto.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.m7.forecasto.data.model.City

@Dao
interface CityDao {

    @Insert(onConflict = REPLACE)
    suspend fun saveCity(city: City)

    @Delete
    suspend fun removeCity(city: City)

    @Query("SELECT * FROM City")
    fun getSavedCities(): LiveData<List<City>>
}