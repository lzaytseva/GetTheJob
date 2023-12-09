package ru.practicum.android.diploma.favorites.data

import ru.practicum.android.diploma.core.data.room.AppDatabase
import ru.practicum.android.diploma.favorites.domain.api.FavoritesVacancyListRepository
import ru.practicum.android.diploma.search.domain.model.VacancyInList
import javax.inject.Inject

class FavoritesVacancyListRepositoryImpl @Inject constructor(
    private val appDatabase: AppDatabase
) : FavoritesVacancyListRepository {
    override suspend fun getFavoritesVacancyList(): VacancyInList {
        return appDatabase.vacancyDao.getVacancyList().map { VacancyShortMapper.map(it) }
    }
}
