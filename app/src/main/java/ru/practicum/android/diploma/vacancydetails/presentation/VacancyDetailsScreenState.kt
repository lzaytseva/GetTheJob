package ru.practicum.android.diploma.vacancydetails.presentation

import ru.practicum.android.diploma.core.domain.models.VacancyDetails

sealed interface VacancyDetailsScreenState {
    object Error : VacancyDetailsScreenState

    object Loading : VacancyDetailsScreenState

    data class Content(val vacancyDetails: VacancyDetails) : VacancyDetailsScreenState
}
