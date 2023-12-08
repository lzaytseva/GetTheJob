package ru.practicum.android.diploma.core.data.network

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.QueryMap
import ru.practicum.android.diploma.BuildConfig.HH_ACCESS_TOKEN
import ru.practicum.android.diploma.core.data.dto.VacancyDetailsDto
import ru.practicum.android.diploma.search.data.responses.VacancySearchResponse

interface HhApiService {

    @GET("/vacancies?access_token=$HH_ACCESS_TOKEN")
    fun getVacancies(
        @QueryMap params: Map<String, String>
    ): Call<VacancySearchResponse>

    @Headers(
        "Authorization: Bearer $HH_ACCESS_TOKEN",
        "HH-User-Agent: GetTheJob (lvzaytseva1@gmail.com)"
    )
    @GET("/vacancies/{vacancy_id}")
    suspend fun getVacancyDetailsById(@Path("vacancy_id") vacancyId: String): Response<VacancyDetailsDto>

}
