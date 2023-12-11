package ru.practicum.android.diploma.favorites.domain.api

import ru.practicum.android.diploma.search.domain.model.VacancyInList

interface FavoritesVacancyListRepository {
    suspend fun getFavoritesVacancyList(): List<VacancyInList>
}
