package ru.practicum.android.diploma.core.data.dto

import ru.practicum.android.diploma.core.data.dto.support_dto.Contacts
import ru.practicum.android.diploma.core.data.dto.support_dto.Experience
import ru.practicum.android.diploma.core.data.dto.support_dto.Salary
import ru.practicum.android.diploma.core.data.dto.support_dto.Schedule

data class VacancyFullDto(
    val id: String,
    val name: String,
    val area: AreaDto,
    val salary: Salary?,
    val experience: Experience?,
    val schedule: Schedule,
    val contacts: Contacts?
)
