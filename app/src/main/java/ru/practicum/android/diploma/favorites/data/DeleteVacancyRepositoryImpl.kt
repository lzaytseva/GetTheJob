package ru.practicum.android.diploma.favorites.data

import ru.practicum.android.diploma.core.data.room.AppDatabase
import ru.practicum.android.diploma.favorites.domain.api.DeleteVacancyRepository
import javax.inject.Inject

class DeleteVacancyRepositoryImpl @Inject constructor(
    private val appDatabase: AppDatabase
) : DeleteVacancyRepository {

    override suspend fun deleteVacancy(vacancyId: String) {
        appDatabase.vacancyDao.deleteVacancy(vacancyId)
    }
}
