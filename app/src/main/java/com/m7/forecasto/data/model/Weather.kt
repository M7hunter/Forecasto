package com.m7.forecasto.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Weather(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var cityId: Int,
    var lat: Double,
    var lon: Double,
    val main: String,
    val description: String,
    val icon: String
)
