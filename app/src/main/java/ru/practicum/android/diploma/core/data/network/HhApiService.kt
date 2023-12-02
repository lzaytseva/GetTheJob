package ru.practicum.android.diploma.core.data.network

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap
import ru.practicum.android.diploma.BuildConfig.HH_ACCESS_TOKEN
import ru.practicum.android.diploma.core.data.dto.AreaDto
import ru.practicum.android.diploma.core.data.dto.VacancyFullDto
import ru.practicum.android.diploma.search.data.responses.IndustriesResponse
import ru.practicum.android.diploma.search.data.responses.SearchRegionResponse
import ru.practicum.android.diploma.search.data.responses.VacancySearchResponse

interface HhApiService {

    @GET("/vacancies?access_token=$HH_ACCESS_TOKEN")
    fun getVacancies(
        @QueryMap params: Map<String, String>
    ): VacancySearchResponse

    @GET("/vacancies/{id}/similar_vacancies?access_token=$HH_ACCESS_TOKEN")
    fun getSimilarVacancies(
        @Path("id") vacancyId: String
    ): VacancySearchResponse

    @GET("/vacancies/{id}?access_token=$HH_ACCESS_TOKEN")
    fun getVacancy(
        @Path("id") vacancyId: String,
    ): VacancyFullDto

    @GET("/areas/countries?access_token=$HH_ACCESS_TOKEN")
    fun getCountries(): List<AreaDto>

    @GET("/areas/{id}?access_token=$HH_ACCESS_TOKEN")
    fun getRegions(
        @Path("id") id: String,
    ): List<AreaDto>

    @GET("/suggests/area_leaves?access_token=$HH_ACCESS_TOKEN")
    fun searchRegions(
        @Query("text") text: String,
        @Query("area_id") areaId: String
    ): SearchRegionResponse

    @GET("/industries?access_token=$HH_ACCESS_TOKEN")
    fun getIndustries(
        @Query("locale") locale: String
    ): IndustriesResponse

}
