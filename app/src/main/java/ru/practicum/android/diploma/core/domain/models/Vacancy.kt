package ru.practicum.android.diploma.core.domain.models

data class Vacancy(
    val id: String,
    val name: String,
    val area: String,
    val salaryCurrency: String?,
    val salaryFrom: Int?,
    val salaryTo: Int?,
    val logoUrl90: String?,
    val address: String?,
    val employerName: String?,
)
