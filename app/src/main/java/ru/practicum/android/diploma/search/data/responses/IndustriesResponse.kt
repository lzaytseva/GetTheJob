package ru.practicum.android.diploma.search.data.responses

import ru.practicum.android.diploma.core.data.dto.IndustryDto

data class IndustriesResponse(
    val industries: List<IndustryDto>, // flatMap in retrofitClient or in SearchRepository
)

// поиск производить без запроса в сеть по уже полученным результатам.
