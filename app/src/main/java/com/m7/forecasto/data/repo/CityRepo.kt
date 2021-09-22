package com.m7.forecasto.data.repo

import com.m7.forecasto.data.model.City
import javax.inject.Inject

class CityRepo @Inject constructor() {

    suspend fun getSavedCities(): List<City> {
        // fake list
        return emptyList()
    }
}