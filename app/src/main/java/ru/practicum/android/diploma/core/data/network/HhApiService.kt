package ru.practicum.android.diploma.core.data.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap
import ru.practicum.android.diploma.core.data.dto.VacancyDetailsDto
import ru.practicum.android.diploma.filters.data.dto.AreaDto
import ru.practicum.android.diploma.filters.data.dto.CountryDto
import ru.practicum.android.diploma.filters.data.dto.ParentIndustryDto
import ru.practicum.android.diploma.search.data.responses.VacancySearchResponse

interface HhApiService {

    @GET("/vacancies")
    suspend fun getVacancies(@QueryMap params: Map<String, String>): Response<VacancySearchResponse>

    @GET("/vacancies/{vacancy_id}")
    suspend fun getVacancyDetailsById(@Path("vacancy_id") vacancyId: String): Response<VacancyDetailsDto>

    @GET("/vacancies/{vacancy_id}/similar_vacancies")
    suspend fun getSimilarVacanciesById(@Path("vacancy_id") vacancyId: String): Response<VacancySearchResponse>

    @GET("/industries")
    suspend fun getIndustries(): Response<List<ParentIndustryDto>>

    @GET("areas/countries")
    suspend fun getCountries(): Response<List<CountryDto>>

    @GET("areas")
    suspend fun getAllAreas(): Response<List<AreaDto>>
}
