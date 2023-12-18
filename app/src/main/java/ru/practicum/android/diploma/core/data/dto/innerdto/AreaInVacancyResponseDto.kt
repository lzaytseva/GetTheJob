package ru.practicum.android.diploma.core.data.dto.innerdto

import com.google.gson.annotations.SerializedName

data class AreaInVacancyResponseDto(
    @SerializedName("id")
    val areaId: String,
    @SerializedName("name")
    val areaName: String
)
