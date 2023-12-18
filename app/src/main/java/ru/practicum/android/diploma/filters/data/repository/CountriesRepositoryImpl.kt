package ru.practicum.android.diploma.filters.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.core.data.network.NetworkClient
import ru.practicum.android.diploma.core.data.network.RetrofitNetworkClient
import ru.practicum.android.diploma.core.domain.api.GetDataRepo
import ru.practicum.android.diploma.core.domain.models.ErrorType
import ru.practicum.android.diploma.filters.data.dto.AllAreasRequest
import ru.practicum.android.diploma.filters.data.dto.AllAreasResponse
import ru.practicum.android.diploma.filters.data.mapper.AreaMapper
import ru.practicum.android.diploma.filters.data.mapper.CountryMapper
import ru.practicum.android.diploma.filters.domain.model.Country
import ru.practicum.android.diploma.util.Resource

class CountriesRepositoryImpl(
    private val networkClient: NetworkClient
) : GetDataRepo<Resource<List<Country>>> {
    override fun get(): Flow<Resource<List<Country>>?> = flow {

        val response = networkClient.doRequest(AllAreasRequest)
        when (response.resultCode) {
            RetrofitNetworkClient.CODE_NO_INTERNET -> {
                emit(Resource.Error(ErrorType.NO_INTERNET))
            }

            RetrofitNetworkClient.CODE_SUCCESS -> {
                emit(
                    Resource.Success(
                        CountryMapper.mapList((response as AllAreasResponse).areas).filter { it.parentId == null }
                    )
                )
            }

            else -> {
                emit(Resource.Error(ErrorType.SERVER_ERROR))
            }
        }
    }

}
