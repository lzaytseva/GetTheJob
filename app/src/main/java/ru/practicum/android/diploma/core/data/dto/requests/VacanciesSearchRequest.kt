package ru.practicum.android.diploma.core.data.dto.requests

private const val TEXT = "text"
private const val SALARY = "salary"
private const val AREA = "area"
private const val INDUSTRY = "industry"
private const val ONLY_WITH_SALARY = "only_with_salary"
private const val CURRENCY = "currency"

data class VacanciesSearchRequest(
    val text: String,
    val salary: String? = null,
    val regionId: String? = null,
    val industryId: String? = null,
    val salaryFlag: Boolean? = null,
    val currency: String? = null
) {
    fun toQueryMap(): Map<String, String> = buildMap {
        put(TEXT, text)
        salary?.also { put(SALARY, it) }
        regionId?.also { put(AREA, it) }
        industryId?.also { put(INDUSTRY, it) }
        salaryFlag?.also { put(ONLY_WITH_SALARY, it.toString()) }
        currency?.also { put(CURRENCY, it) }
    }
}
