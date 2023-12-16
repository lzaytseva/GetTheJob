package ru.practicum.android.diploma.favorites.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.search.domain.model.Vacancy

interface FavoritesVacancyListRepository {
    suspend fun getFavoritesVacancyList(): Flow<List<Vacancy>?>
}
