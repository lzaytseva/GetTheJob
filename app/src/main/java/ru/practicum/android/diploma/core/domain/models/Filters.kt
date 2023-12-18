package ru.practicum.android.diploma.core.domain.models

data class Filters(
    val regionId: String? = null,
    val regionName: String? = null,
    val countryId: String? = null,
    val countryName: String? = null,
    val salary: String? = null,
    val salaryFlag: Boolean? = null,
    val industryId: String? = null,
    val industryName: String? = null,
    val currency: String? = null,
)
