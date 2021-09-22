package com.m7.forecasto.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class City(
    val id: Int,
    val name: String,
    val lat: Double,
    val lng: Double,
) : Parcelable
