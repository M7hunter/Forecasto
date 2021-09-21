package com.m7.forecasto.data.repo

import com.m7.forecasto.data.api.APIsImpl
import javax.inject.Inject

class ForecastRepo @Inject constructor(private val apIsImpl: APIsImpl) {

    suspend fun getForecastByLatLng(lat: Double, lng: Double) =
        apIsImpl.getForecastByLatLng(lat, lng)
}