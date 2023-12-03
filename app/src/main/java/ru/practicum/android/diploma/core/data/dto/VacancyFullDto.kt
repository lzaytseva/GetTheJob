package ru.practicum.android.diploma.core.data.dto

import ru.practicum.android.diploma.core.data.dto.innerdto.Contacts
import ru.practicum.android.diploma.core.data.dto.innerdto.Experience
import ru.practicum.android.diploma.core.data.dto.innerdto.Salary
import ru.practicum.android.diploma.core.data.dto.innerdto.Schedule

data class VacancyFullDto(
    val id: String,
    val name: String,
    val area: AreaDto,
    val salary: Salary?,
    val experience: Experience?,
    val schedule: Schedule,
    val contacts: Contacts?
)
