package ru.practicum.android.diploma.search.data.responses

import com.google.gson.annotations.SerializedName
import ru.practicum.android.diploma.core.data.dto.VacancySearchDto

data class VacancySearchResponse(
    val items: List<VacancySearchDto>,
    val found: Int, // number of vacancies
    val pages: Int, // number of pages
    val page: Int, // current page
    @SerializedName("per_page")
    val perPage: Int
)
