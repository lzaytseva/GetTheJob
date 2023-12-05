package ru.practicum.android.diploma.search.domain.model

import ru.practicum.android.diploma.core.data.dto.innerdto.EmployerDto
import ru.practicum.android.diploma.core.data.dto.innerdto.SalaryDto

data class VacancyInList(
    val id: String,
    val name: String,
    val salaryDto: SalaryDto?,
    val employerDto: EmployerDto
)
