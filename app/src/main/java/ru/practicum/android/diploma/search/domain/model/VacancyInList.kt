package ru.practicum.android.diploma.search.domain.model

import ru.practicum.android.diploma.core.data.dto.innerdto.Employer
import ru.practicum.android.diploma.core.data.dto.innerdto.Salary

data class VacancyInList(
    val id: String,
    val name: String,
    val salary: Salary?,
    val employer: Employer
)
