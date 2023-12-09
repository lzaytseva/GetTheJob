package ru.practicum.android.diploma.vacancydetails.domain.api

import ru.practicum.android.diploma.core.domain.models.VacancyDetails

interface SaveVacancyRepository {

    suspend fun saveVacancy(vacancyDetails: VacancyDetails)
}
