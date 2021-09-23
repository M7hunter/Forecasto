package com.m7.forecasto.data.repo

import com.m7.forecasto.data.db.CityDao
import com.m7.forecasto.data.model.City
import javax.inject.Inject

class CityRepo @Inject constructor(private val cityDao: CityDao) {

    suspend fun saveCity(city: City) = cityDao.saveCity(city)

    suspend fun removeCity(city: City) = cityDao.removeCity(city)

    fun getSavedCities() = cityDao.getSavedCities()
}