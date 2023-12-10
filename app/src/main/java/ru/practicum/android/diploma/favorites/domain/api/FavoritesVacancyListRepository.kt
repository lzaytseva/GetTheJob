package ru.practicum.android.diploma.favorites.domain.api

import ru.practicum.android.diploma.core.domain.models.VacancyInList

interface FavoritesVacancyListRepository {
    suspend fun getFavoritesVacancyList(): List<VacancyInList>
}
