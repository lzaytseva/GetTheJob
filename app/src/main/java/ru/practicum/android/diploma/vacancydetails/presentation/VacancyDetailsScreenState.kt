package ru.practicum.android.diploma.vacancydetails.presentation

import ru.practicum.android.diploma.core.domain.models.ErrorType
import ru.practicum.android.diploma.core.domain.models.VacancyDetails

sealed interface VacancyDetailsScreenState {
    data class Error(val error: ErrorType?) : VacancyDetailsScreenState

    object Loading : VacancyDetailsScreenState

    data class Content(val vacancyDetails: VacancyDetails) : VacancyDetailsScreenState
}
