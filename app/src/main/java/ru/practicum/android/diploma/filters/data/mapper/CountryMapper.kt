package ru.practicum.android.diploma.filters.data.mapper

import ru.practicum.android.diploma.filters.data.dto.CountryDto
import ru.practicum.android.diploma.filters.domain.model.Country

object CountryMapper {

    fun map(countryDto: CountryDto) = Country(
        id = countryDto.id,
        name = countryDto.name
    )

    fun mapList(countriesDto: List<CountryDto>): List<Country> {
        val countries = mutableListOf<Country>()
        countriesDto.forEach { dto ->
            countries.add(map(dto))
        }
        return countries
    }
}
