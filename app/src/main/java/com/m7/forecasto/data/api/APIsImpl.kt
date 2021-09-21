package com.m7.forecasto.data.api

import com.m7.forecasto.BuildConfig
import javax.inject.Inject

class APIsImpl @Inject constructor(private val apis: APIs) {

    suspend fun getForecastByLatLng(
        lat: Double,
        lng: Double,
        apiKey: String = BuildConfig.API_KEY,
        exclude: String = "current,minutely,hourly,alerts"
    ) = apis.getForecastByLatLng(lat, lng, apiKey, exclude)
}