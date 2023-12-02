package ru.practicum.android.diploma.core.data.dto

import ru.practicum.android.diploma.core.data.dto.supportDto.Employer
import ru.practicum.android.diploma.core.data.dto.supportDto.Salary

data class VacancySearchDto(
    val id: String,
    val name: String,
    val salary: Salary?,
    val employer: Employer
)
