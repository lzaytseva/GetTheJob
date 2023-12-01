package ru.practicum.android.diploma.core.data.dto

import com.google.gson.annotations.SerializedName

data class Employer(
    @SerializedName("accredited_it_employer")
    val accreditedItEmployer: Boolean,
    @SerializedName("alternate_url")
    val alternateUrl: String,
    val id: String,
    @SerializedName("logo_urls")
    val logoUrls: LogoUrls,
    val name: String,
    val trusted: Boolean,
    val url: String
)
