package ru.practicum.android.diploma.filters.data.mapper

import ru.practicum.android.diploma.filters.data.dto.ParentIndustryDto
import ru.practicum.android.diploma.filters.domain.model.Industry

object IndustryMapper {
    fun mapDtoToEntityList(industriesDto: List<ParentIndustryDto>): List<Industry> {
        val industries = mutableListOf<Industry>()
        industriesDto.forEach { parentIndustry ->
            parentIndustry.subIndustries.forEach { industry ->
                industries.add(
                    Industry(
                        id = industry.id,
                        name = industry.name
                    )
                )
            }
        }
        return industries
    }
}
