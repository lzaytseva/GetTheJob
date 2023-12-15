package ru.practicum.android.diploma.favorites.presentation

import ru.practicum.android.diploma.search.domain.model.VacancyInList

sealed interface FavoritesState {
    object Empty : FavoritesState
    object DbError : FavoritesState
    data class Content(val vacancies: List<VacancyInList>) : FavoritesState
}
