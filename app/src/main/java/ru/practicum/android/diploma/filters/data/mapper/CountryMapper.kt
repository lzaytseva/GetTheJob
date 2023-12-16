package ru.practicum.android.diploma.filters.data.mapper

import ru.practicum.android.diploma.filters.data.dto.CountryDto
import ru.practicum.android.diploma.filters.domain.model.Area

object CountryMapper {

    fun map(countryDto: CountryDto) = Area(
        id = countryDto.id,
        name = countryDto.name
    )

    fun mapList(countriesDto: List<CountryDto>): List<Area> {
        val countries = mutableListOf<Area>()
        countriesDto.forEach { dto ->
            countries.add(map(dto))
        }
        return countries
    }
}
