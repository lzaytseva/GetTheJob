package ru.practicum.android.diploma.core.data.dto

import ru.practicum.android.diploma.core.data.dto.supportDto.Contacts
import ru.practicum.android.diploma.core.data.dto.supportDto.Experience
import ru.practicum.android.diploma.core.data.dto.supportDto.Salary
import ru.practicum.android.diploma.core.data.dto.supportDto.Schedule

data class VacancyFullDto(
    val id: String,
    val name: String,
    val area: AreaDto,
    val salary: Salary?,
    val experience: Experience?,
    val schedule: Schedule,
    val contacts: Contacts?
)
