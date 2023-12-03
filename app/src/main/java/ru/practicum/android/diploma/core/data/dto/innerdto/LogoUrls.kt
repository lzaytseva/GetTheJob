package ru.practicum.android.diploma.core.data.dto.innerdto

import com.google.gson.annotations.SerializedName

data class LogoUrls(
    @SerializedName("90")
    val art90: String?,
    @SerializedName("240")
    val art240: String?,
    val original: String
)
