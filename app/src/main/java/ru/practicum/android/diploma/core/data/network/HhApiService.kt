package ru.practicum.android.diploma.core.data.network

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.practicum.android.diploma.core.data.dto.AreaDto
import ru.practicum.android.diploma.search.data.responses.SearchRegionResponse
import ru.practicum.android.diploma.core.data.dto.VacancyFullDto
import ru.practicum.android.diploma.search.data.responses.VacancySearchResponse

interface HhApiService {

    @GET("/vacancies")
    fun getVacancies(
        @Query("access_token") accessToken: String,
        @Query("text") text: String,
        @Query("area") areaId: String
    )

    @GET("/vacancies")
    fun getVacancies(
        @Query("access_token") accessToken: String,
        @Query("text") text: String
    ): VacancySearchResponse

    @GET("/vacancies/{id}")
    fun getVacancy(
        @Path("id") vacancyId: String,
        @Query("access_token") accessToken: String
    ): VacancyFullDto

    @GET("/areas/countries")
    fun getCountries(
        @Query("access_token") accessToken: String,
        @Query("text") query: String
    ): List<AreaDto>

    @GET("/areas/{id}")
    fun getRegions(
        @Path("id") id: String,
        @Query("access_token") accessToken: String
    ): List<AreaDto>

    @GET("/suggests/area_leaves")
    fun searchRegions(
        @Query("access_token") accessToken: String,
        @Query("text") text: String,
        @Query("area_id") areaId: String
    ): SearchRegionResponse

}
