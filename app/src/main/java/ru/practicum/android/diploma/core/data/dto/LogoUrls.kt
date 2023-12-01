package ru.practicum.android.diploma.core.data.dto

import com.google.gson.annotations.SerializedName

data class LogoUrls(
    @SerializedName("240")
    val art240: String,
    @SerializedName("90")
    val art90: String,
    val original: String
)
