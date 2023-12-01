package ru.practicum.android.diploma.core.data.dto

import com.google.gson.annotations.SerializedName

data class MetroStation(
    val lat: Double,
    @SerializedName("line_id")
    val lineId: String,
    @SerializedName("line_name")
    val lineName: String,
    val lng: Double,
    @SerializedName("station_id")
    val stationId: String,
    @SerializedName("station_name")
    val stationName: String
)
