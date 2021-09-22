package com.m7.forecasto.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class City(
    @PrimaryKey
    val id: Int,
    val lat: Double,
    val lng: Double,
    val name: String? = null,
    var isFavorite: Boolean = false,
) : Parcelable
