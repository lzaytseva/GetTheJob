package ru.practicum.android.diploma.core.data.dto

import com.google.gson.annotations.SerializedName

data class Address(
    val building: String,
    val city: String,
    val description: String,
    val lat: Double,
    val lng: Double,
    @SerializedName("metro_stations")
    val metroStations: List<MetroStation>,
    val street: String
)
