package ru.practicum.android.diploma.core.data.dto

import com.google.gson.annotations.SerializedName

data class RegionDto(
    val id: String,
    @SerializedName("text")
    val name: String
)
