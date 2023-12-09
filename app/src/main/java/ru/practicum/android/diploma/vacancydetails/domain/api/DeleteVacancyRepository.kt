package ru.practicum.android.diploma.vacancydetails.domain.api

interface DeleteVacancyRepository {

    suspend fun deleteVacancy(vacancyId: String)
}
