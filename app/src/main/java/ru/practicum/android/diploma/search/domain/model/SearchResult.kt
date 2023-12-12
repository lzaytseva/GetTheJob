package ru.practicum.android.diploma.search.domain.model

data class SearchResult(
    val vacancies: List<VacancyInList>,
    val pages: Int,
    val found: Int
)
