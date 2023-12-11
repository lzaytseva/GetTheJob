package ru.practicum.android.diploma.core.data.dto.requests

data class VacanciesSearchRequest(
    val text: String,
    val salary: String? = null,
    val regionId: String? = null,
    val industryId: String? = null,
    val salaryFlag: Boolean? = null,
    val currency: String? = null,
    val perPage: String = "20",
    val page: Int? = null
)
