package ru.practicum.android.diploma.core.data.dto

import ru.practicum.android.diploma.core.data.dto.innerdto.EmployerDto
import ru.practicum.android.diploma.core.data.dto.innerdto.SalaryDto

data class VacancySearchDto(
    val id: String,
    val name: String,
    val salaryDto: SalaryDto?,
    val employerDto: EmployerDto
)
