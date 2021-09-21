package com.m7.forecasto.data.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity
data class Forecast(

    @PrimaryKey
    val id: Int,
    val lat: Double,
    val lon: Double,

    @Ignore
    val daily: List<Daily>
)
