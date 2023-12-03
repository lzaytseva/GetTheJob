package ru.practicum.android.diploma.core.data.dto

import com.google.gson.annotations.SerializedName

data class AreaDto(
    val id: String,
    @SerializedName(value = "name", alternate = ["text"])
    val name: String
)
