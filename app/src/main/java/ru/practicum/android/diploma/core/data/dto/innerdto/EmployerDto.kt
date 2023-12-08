package ru.practicum.android.diploma.core.data.dto.innerdto

import com.google.gson.annotations.SerializedName

data class EmployerDto(
    val name: String,
    @SerializedName("logo_urls") val logoUrlsDto: LogoUrlsDto?,
    @SerializedName("alternate_url") val alternateUrl: String
)
