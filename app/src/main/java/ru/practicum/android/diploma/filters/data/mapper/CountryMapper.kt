package ru.practicum.android.diploma.filters.data.mapper

import ru.practicum.android.diploma.filters.data.dto.AreaDto
import ru.practicum.android.diploma.filters.domain.model.Country

object CountryMapper {

    fun map(areasDto: AreaDto) = Country(
        id = areasDto.id,
        name = areasDto.name,
        parentId = areasDto.parentId
    )

    fun mapList(areasDto: List<AreaDto>): List<Country> {
        val countries = mutableListOf<Country>()
        areasDto.forEach { dto ->
            countries.add(map(dto))
        }
        return countries
    }
}
