package ru.practicum.android.diploma.vacancydetails.presentation

import ru.practicum.android.diploma.core.domain.models.ErrorType
import ru.practicum.android.diploma.search.domain.model.Vacancy

sealed interface SimilarVacanciesScreenState {
    data class Error(val message: ErrorType?) : SimilarVacanciesScreenState

    object Loading : SimilarVacanciesScreenState

    data class Content(val vacancies: List<Vacancy>) : SimilarVacanciesScreenState
}
