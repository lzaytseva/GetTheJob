package ru.practicum.android.diploma.favorites.data

import ru.practicum.android.diploma.core.data.room.AppDatabase
import ru.practicum.android.diploma.core.domain.models.VacancyDetails
import ru.practicum.android.diploma.favorites.domain.api.VacancyDetailsRepository
import javax.inject.Inject

class VacancyDetailsRepositoryImpl @Inject constructor(
    private val appDatabase: AppDatabase
) : VacancyDetailsRepository {
    override suspend fun getVacancyById(vacancyId: String): VacancyDetails {
        return VacancyEntityMapper.map(appDatabase.vacancyDao.getVacancyById(vacancyId))
    }
}
