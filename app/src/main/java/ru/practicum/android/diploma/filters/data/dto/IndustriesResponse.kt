package ru.practicum.android.diploma.filters.data.dto

import ru.practicum.android.diploma.core.data.dto.responses.Response

data class IndustriesResponse(
    val industries: List<ParentIndustryDto>
) : Response()
