package ru.practicum.android.diploma.favorites.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.practicum.android.diploma.core.data.room.VacancyShortMapper
import ru.practicum.android.diploma.core.data.room.dao.VacancyDao
import ru.practicum.android.diploma.core.domain.api.GetDataRepo
import ru.practicum.android.diploma.search.domain.model.Vacancy

class FavoritesVacancyListRepositoryImpl(
    private val vacancyDao: VacancyDao
) : GetDataRepo<List<Vacancy>> {

    override fun get(): Flow<List<Vacancy>?> =
        vacancyDao.getVacancyList().map { list ->
            list.map { item ->
                VacancyShortMapper.map(item)
            }
        }
}
