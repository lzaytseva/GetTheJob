package ru.practicum.android.diploma.core.domain.models

data class VacancyInList(
    val id: String,
    val name: String,
    val salaryCurrency: String?,
    val salaryFrom: Int?,
    val salaryTo: Int?,
    val employerName: String,
    val logo: String?
)
