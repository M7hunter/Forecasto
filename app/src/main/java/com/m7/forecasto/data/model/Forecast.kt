package com.m7.forecasto.data.model

data class Forecast(
    val id: Int,
    val lat: Double,
    val lon: Double,
    val daily: List<Daily>
)
