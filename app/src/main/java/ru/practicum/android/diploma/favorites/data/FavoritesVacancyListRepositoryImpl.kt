package ru.practicum.android.diploma.favorites.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.practicum.android.diploma.core.data.room.AppDatabase
import ru.practicum.android.diploma.core.data.room.VacancyShortMapper
import ru.practicum.android.diploma.favorites.domain.api.FavoritesVacancyListRepository
import ru.practicum.android.diploma.search.domain.model.Vacancy
import javax.inject.Inject

class FavoritesVacancyListRepositoryImpl @Inject constructor(
    private val appDatabase: AppDatabase
) : FavoritesVacancyListRepository {

    override suspend fun getFavoritesVacancyList(): Flow<List<Vacancy>?> =
        appDatabase.vacancyDao.getVacancyList().map { list ->
            list.map { item ->
                VacancyShortMapper.map(item)
            }
        }
}
