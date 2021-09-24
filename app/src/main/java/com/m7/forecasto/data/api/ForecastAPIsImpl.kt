package com.m7.forecasto.data.api

import com.m7.forecasto.BuildConfig
import javax.inject.Inject

class ForecastAPIsImpl @Inject constructor(private val apis: ForecastAPIs) {

    suspend fun getForecastByLatLng(
        lat: Double,
        lng: Double,
        apiKey: String = BuildConfig.FORECAST_API_KEY,
        exclude: String = "current,minutely,hourly,alerts"
    ) = apis.getForecastByLatLng(lat, lng, apiKey, exclude)
}