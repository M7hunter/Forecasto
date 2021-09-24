package com.m7.forecasto.data.model

data class Suggestion(
    val formatted_address: String?,
    val geometry: Geometry?,
    val name: String,
) {

    data class Geometry(
        val location: Location
    ) {
        data class Location(
            val lat: String,
            val lng: String,
        )
    }
}
