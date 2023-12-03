package ru.practicum.android.diploma.core.data.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.QueryMap
import ru.practicum.android.diploma.BuildConfig.HH_ACCESS_TOKEN
import ru.practicum.android.diploma.search.data.responses.VacancySearchResponse

interface HhApiService {

    @GET("/vacancies?access_token=$HH_ACCESS_TOKEN")
    fun getVacancies(
        @QueryMap params: Map<String, String>
    ): Call<VacancySearchResponse>

}
