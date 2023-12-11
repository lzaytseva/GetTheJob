package ru.practicum.android.diploma.search.util

import ru.practicum.android.diploma.core.data.dto.VacancySearchDto
import ru.practicum.android.diploma.search.domain.model.VacancyInList

fun VacancySearchDto.toVacancyInList(): VacancyInList =
    VacancyInList(
        id = id,
        name = name,
        salaryFrom = salaryDto?.from,
        salaryTo = salaryDto?.to,
        salaryGross = salaryDto?.gross,
        salaryCurrency = salaryDto?.currency,
        employerName = employerDto.name,
        logo = employerDto.logoUrlsDto?.art90
    )
