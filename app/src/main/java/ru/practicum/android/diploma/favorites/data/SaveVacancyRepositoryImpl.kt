package ru.practicum.android.diploma.favorites.data

import ru.practicum.android.diploma.core.data.room.AppDatabase
import ru.practicum.android.diploma.core.domain.models.VacancyDetails
import ru.practicum.android.diploma.favorites.domain.api.SaveVacancyRepository
import javax.inject.Inject

class SaveVacancyRepositoryImpl @Inject constructor(
    private val appDatabase: AppDatabase
) : SaveVacancyRepository {

    override suspend fun saveVacancy(vacancyDetails: VacancyDetails) {
        appDatabase.vacancyDao.saveVacancy(VacancyEntityMapper.map(vacancyDetails))
    }
}
