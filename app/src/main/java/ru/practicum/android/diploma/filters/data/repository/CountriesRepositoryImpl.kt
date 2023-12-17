package ru.practicum.android.diploma.filters.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.core.data.network.NetworkClient
import ru.practicum.android.diploma.core.data.network.RetrofitNetworkClient
import ru.practicum.android.diploma.core.domain.api.GetDataByIdRepo
import ru.practicum.android.diploma.core.domain.api.GetDataRepo
import ru.practicum.android.diploma.core.domain.models.ErrorType
import ru.practicum.android.diploma.filters.data.dto.CountriesRequest
import ru.practicum.android.diploma.filters.data.dto.CountriesResponse
import ru.practicum.android.diploma.filters.data.mapper.CountryMapper
import ru.practicum.android.diploma.filters.domain.model.Country
import ru.practicum.android.diploma.util.Resource

class CountriesRepositoryImpl(
    private val networkClient: NetworkClient
) : GetDataRepo<Resource<List<Country>>>, GetDataByIdRepo<Resource<Country>> {
    override fun get(): Flow<Resource<List<Country>>?> = flow {
        val response = networkClient.doRequest(CountriesRequest)

        when (response.resultCode) {
            RetrofitNetworkClient.CODE_NO_INTERNET -> emit(Resource.Error(ErrorType.NO_INTERNET))

            RetrofitNetworkClient.CODE_SUCCESS -> {
                val countries = CountryMapper.mapList((response as CountriesResponse).countries)
                emit(Resource.Success(countries))
            }

            else -> emit(Resource.Error(ErrorType.SERVER_ERROR))

        }
    }

    override fun getById(id: String): Flow<Resource<Country>?> = flow {
        val response = networkClient.doRequest(CountriesRequest)

        when (response.resultCode) {
            RetrofitNetworkClient.CODE_NO_INTERNET -> emit(Resource.Error(ErrorType.NO_INTERNET))

            RetrofitNetworkClient.CODE_SUCCESS -> {
                val countries = CountryMapper.mapList((response as CountriesResponse).countries)
                val result = countries.find { country ->
                    country.id == id
                }
                if (result != null) {
                    emit(Resource.Success(result))
                } else {
                    emit(Resource.Error(ErrorType.NO_CONTENT))
                }
            }

            else -> emit(Resource.Error(ErrorType.SERVER_ERROR))

        }
    }
}
