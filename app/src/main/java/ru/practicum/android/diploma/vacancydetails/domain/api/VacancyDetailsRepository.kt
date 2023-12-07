package ru.practicum.android.diploma.vacancydetails.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.core.domain.models.VacancyDetails
import ru.practicum.android.diploma.util.Resource

interface VacancyDetailsRepository {

    fun getVacancyDetailsById(vacancyId: String) : Flow<Resource<VacancyDetails>>
}
