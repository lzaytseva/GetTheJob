package ru.practicum.android.diploma.favorites.domain.api

interface DeleteVacancyRepository {

    suspend fun deleteVacancy(vacancyId: String)
}
