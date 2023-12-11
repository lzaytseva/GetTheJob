package ru.practicum.android.diploma.search.data.responses

import com.google.gson.annotations.SerializedName
import ru.practicum.android.diploma.core.data.dto.VacancySearchDto
import ru.practicum.android.diploma.core.data.dto.responses.Response

data class VacancySearchResponse(
    val items: List<VacancySearchDto>,
    val found: Int, // number of vacancies
    val page: Int, // current page
    val pages: Int, // number of pages
    @SerializedName("per_page")
    val perPage: Int
) : Response()
