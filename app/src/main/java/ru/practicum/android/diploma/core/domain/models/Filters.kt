package ru.practicum.android.diploma.core.domain.models

data class Filters(
    val regionId: String?,
    val regionName: String?,
    val countryId: String?,
    val countryName: String?,
    val salary: String?,
    val salaryFlag: Boolean?,
    val industryId: String?,
    val industryName: String?,
    val currency: String?
)
