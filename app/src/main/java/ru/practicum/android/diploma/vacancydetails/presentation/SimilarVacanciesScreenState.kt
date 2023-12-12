package ru.practicum.android.diploma.vacancydetails.presentation

import ru.practicum.android.diploma.search.domain.model.VacancyInList

sealed interface SimilarVacanciesScreenState {
    object Error : SimilarVacanciesScreenState

    object Loading : SimilarVacanciesScreenState

    data class Content(val vacancies: List<VacancyInList>) : SimilarVacanciesScreenState
}
