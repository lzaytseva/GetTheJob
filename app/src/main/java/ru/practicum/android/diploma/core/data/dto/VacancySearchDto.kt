package ru.practicum.android.diploma.core.data.dto

import ru.practicum.android.diploma.core.data.dto.innerdto.EmployerDto
import ru.practicum.android.diploma.core.data.dto.innerdto.SalaryDto
import ru.practicum.android.diploma.core.data.dto.innerdto.VacancyElementDto

data class VacancySearchDto(
    val id: String,
    val name: String,
    val salary: SalaryDto?,
    val employer: EmployerDto?,
    val area: VacancyElementDto,
)
