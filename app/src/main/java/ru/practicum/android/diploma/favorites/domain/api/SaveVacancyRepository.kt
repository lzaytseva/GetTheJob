package ru.practicum.android.diploma.favorites.domain.api

import ru.practicum.android.diploma.core.domain.models.VacancyDetails

interface SaveVacancyRepository {

    suspend fun saveVacancy(vacancyDetails: VacancyDetails)
}
