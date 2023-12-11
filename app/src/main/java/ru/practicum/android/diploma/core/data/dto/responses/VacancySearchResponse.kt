package ru.practicum.android.diploma.core.data.dto.responses

import ru.practicum.android.diploma.core.data.dto.VacancySearchDto

data class VacancySearchResponse(
    val dtos: List<VacancySearchDto>
) : Response()
