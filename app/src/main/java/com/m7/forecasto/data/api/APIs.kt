package com.m7.forecasto.data.api

import com.m7.forecasto.data.model.Forecast
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface APIs {

    @GET("onecall")
    suspend fun getForecastByLatLng(
        @Query("lat") lat: Double,
        @Query("lon") lng: Double,
        @Query("appid") apiKey: String,
        @Query("exclude") exclude: String
    ): Response<Forecast>
}