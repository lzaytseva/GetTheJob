package ru.practicum.android.diploma.search.util

import ru.practicum.android.diploma.core.data.dto.VacancySearchDto
import ru.practicum.android.diploma.core.data.dto.requests.VacanciesSearchRequest
import ru.practicum.android.diploma.search.domain.model.Vacancy

private const val TEXT = "text"
private const val SALARY = "salary"
private const val AREA = "area"
private const val INDUSTRY = "industry"
private const val ONLY_WITH_SALARY = "only_with_salary"
private const val CURRENCY = "currency"
private const val PER_PAGE = "per_page"
private const val PAGE = "page"

fun VacancySearchDto.toVacancyInList(): Vacancy =
    Vacancy(
        id = id,
        name = name,
        salaryFrom = salaryDto?.from,
        salaryTo = salaryDto?.to,
        salaryGross = salaryDto?.gross,
        salaryCurrency = salaryDto?.currency,
        employerName = employerDto.name,
        logo = employerDto.logoUrlsDto?.art240
    )

fun VacanciesSearchRequest.toQueryMap(): Map<String, String> = buildMap {
    put(TEXT, text)
    put(PER_PAGE, perPage.toString())
    page?.also { put(PAGE, it.toString()) }
    salary?.also { put(SALARY, it) }
    regionId?.also { put(AREA, it) }
    industryId?.also { put(INDUSTRY, it) }
    salaryFlag?.also { put(ONLY_WITH_SALARY, it.toString()) }
    currency?.also { put(CURRENCY, it) }
}
