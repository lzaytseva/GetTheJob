package ru.practicum.android.diploma.core.domain.models

data class VacancyDetails(
    val id: String,
    val name: String,
    val area: String,
    val salaryCurrency: String,
    val salaryFrom: Int?,
    val salaryTo: Int?,
    val salaryGross: Boolean?,
    val experience: String?,
    val schedule: String?,
    val contactName: String,
    val contactEmail: String,
    val phones: List<String>,
    val logoUrl: String,
    val logoUrl90: String,
    val logoUrl240: String,
    val address: String,
    val employerUrl: String,
    val employerName: String,
    val employment: String,
    val keySkills: List<String>,
)
