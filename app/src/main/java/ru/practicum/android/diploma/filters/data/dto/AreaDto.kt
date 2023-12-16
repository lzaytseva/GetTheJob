package ru.practicum.android.diploma.filters.data.dto

import com.google.gson.annotations.SerializedName

data class AreaDto(
    val areas: List<AreaDto>,
    val id: String,
    val name: String,
    @SerializedName("parent_id")
    val parentId: String
)
