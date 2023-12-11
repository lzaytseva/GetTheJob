package ru.practicum.android.diploma.core.data.room

import ru.practicum.android.diploma.core.data.room.entity.VacancyShort
import ru.practicum.android.diploma.core.domain.models.VacancyInList

object VacancyShortMapper {

    fun map(vacancyShort: VacancyShort): VacancyInList = VacancyInList(
        id = vacancyShort.vacancyId,
        name = vacancyShort.name,
        salaryCurrency = vacancyShort.salaryCurrency,
        salaryFrom = vacancyShort.salaryFrom,
        salaryTo = vacancyShort.salaryTo,
        employerName = vacancyShort.employerName ?: "",
        logo = vacancyShort.logoUrl
    )
}
