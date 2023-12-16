package ru.practicum.android.diploma.core.domain.models

data class Filters(
    val regionId: String?,
    val salary: String?,
    val salaryFlag: Boolean?,
    val industryId: String?,
    val currency: String?
)
