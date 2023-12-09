package ru.practicum.android.diploma.favorites.domain.api

import ru.practicum.android.diploma.core.domain.models.VacancyDetails

interface VacancyDetailsRepository {
    suspend fun getVacancyById(vacancyId: String): VacancyDetails
}
