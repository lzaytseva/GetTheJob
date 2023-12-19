package ru.practicum.android.diploma.filters.data.mapper

import ru.practicum.android.diploma.filters.data.dto.AreaDto
import ru.practicum.android.diploma.filters.domain.model.Country

object AreaMapper {

    fun mapList(areasDto: List<AreaDto>): List<Country> {
        val subRegions = areasDto.flatMap { it.areas }
            .flatMap { it.areas }
            .map {
                Country(
                    id = it.id,
                    name = it.name,
                    parentId = it.parentId
                )
            }
        val regions = areasDto.flatMap { it.areas }
            .map {
                Country(
                    id = it.id,
                    name = it.name,
                    parentId = it.parentId
                )
            }
        val countries = areasDto.map {
            Country(
                id = it.id,
                name = it.name,
                parentId = it.parentId
            )
        }
        return countries + regions + subRegions
    }

    fun map(country: AreaDto): List<Country> {
        val countries = listOf(
            Country(
                id = country.id,
                name = country.name,
                parentId = country.parentId
            )
        )
        val regions = country.areas.map { region ->
            Country(
                id = region.id,
                name = region.name,
                parentId = region.parentId
            )
        }
        val subRegions = country.areas.flatMap { region ->
            region.areas
        }.map { subRegion ->
            Country(
                id = subRegion.id,
                name = subRegion.name,
                parentId = subRegion.parentId
            )
        }
        return countries + regions + subRegions
    }
}
