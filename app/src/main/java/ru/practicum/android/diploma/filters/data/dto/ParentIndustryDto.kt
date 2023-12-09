package ru.practicum.android.diploma.filters.data.dto

import com.google.gson.annotations.SerializedName

data class ParentIndustryDto(
    @SerializedName("id") val id: String,
    @SerializedName("industries") val subIndustries: List<IndustryDto>,
    @SerializedName("name") val name: String
)
