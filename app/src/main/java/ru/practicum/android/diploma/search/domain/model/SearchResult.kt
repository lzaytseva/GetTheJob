package ru.practicum.android.diploma.search.domain.model

data class SearchResult(
    val vacancies: List<Vacancy>,
    val pages: Int,
    val found: Int
)
