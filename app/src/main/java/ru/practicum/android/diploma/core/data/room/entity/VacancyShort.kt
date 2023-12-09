package ru.practicum.android.diploma.core.data.room.entity

data class VacancyShort(
    val vacancyId: String,
    val name: String,
    val salaryCurrency: String?,
    val salaryFrom: Int?,
    val salaryTo: Int?,
    val employerName: String?,
    val logoUrl: String?
)
