package ru.practicum.android.diploma.filters.data.mapper

import ru.practicum.android.diploma.filters.data.dto.AreaDto
import ru.practicum.android.diploma.filters.domain.model.Country

object AreaMapper {

    fun mapList(areasDto: List<AreaDto>): List<Country> = areasDto.flatMap { country ->
        country.areas.map { region ->
            Country(
                id = region.id,
                name = region.name,
                parentId = if (region.parentId.trim() == "null") {
                    null
                } else {
                    region.parentId
                }
            )
        }
    }

    fun mapRegion(country: AreaDto): List<Country> = country.areas.map { region ->
        Country(
            id = region.id,
            name = region.name,
            parentId = region.parentId
        )
    }
}
