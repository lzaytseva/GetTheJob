package ru.practicum.android.diploma.search.domain.model

data class VacancyInList(
    val id: String,
    val name: String,
    val salaryCurrency: String?,
    val salaryFrom: Int?,
    val salaryTo: Int?,
    val salaryGross: Boolean?,
    val employerName: String,
    val logo: String?
)
