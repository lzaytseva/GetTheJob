package ru.practicum.android.diploma.filters.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.core.data.network.NetworkClient
import ru.practicum.android.diploma.core.data.network.RetrofitNetworkClient
import ru.practicum.android.diploma.core.domain.api.GetDataByIdRepo
import ru.practicum.android.diploma.core.domain.api.GetDataRepo
import ru.practicum.android.diploma.core.domain.models.ErrorType
import ru.practicum.android.diploma.filters.data.dto.AllAreasRequest
import ru.practicum.android.diploma.filters.data.dto.AllAreasResponse
import ru.practicum.android.diploma.filters.data.dto.CountryByIdRequest
import ru.practicum.android.diploma.filters.data.dto.CountryByIdResponse
import ru.practicum.android.diploma.filters.data.mapper.AreaMapper
import ru.practicum.android.diploma.filters.domain.model.Country
import ru.practicum.android.diploma.util.Resource
import javax.inject.Inject

class RegionsRepositoryImpl @Inject constructor(
    private val networkClient: NetworkClient
) : GetDataByIdRepo<Resource<List<Country>>>, GetDataRepo<Resource<List<Country>>> {
    override fun getById(id: String): Flow<Resource<List<Country>>?> = flow {
        val response = networkClient.doRequest(CountryByIdRequest(id))
        when (response.resultCode) {
            RetrofitNetworkClient.CODE_NO_INTERNET -> {
                emit(Resource.Error(ErrorType.NO_INTERNET))
            }

            RetrofitNetworkClient.CODE_SUCCESS -> {
                emit(Resource.Success(AreaMapper.map((response as CountryByIdResponse).country)))
            }

            else -> {
                emit(Resource.Error(ErrorType.SERVER_ERROR))
            }
        }
    }

    override fun get(): Flow<Resource<List<Country>>?> = flow {
        val response = networkClient.doRequest(AllAreasRequest)
        when (response.resultCode) {
            RetrofitNetworkClient.CODE_NO_INTERNET -> {
                emit(Resource.Error(ErrorType.NO_INTERNET))
            }

            RetrofitNetworkClient.CODE_SUCCESS -> {
                emit(
                    Resource.Success(
                        AreaMapper.mapList((response as AllAreasResponse).areas).filter { it.parentId != null }
                    )
                )
            }

            else -> {
                emit(Resource.Error(ErrorType.SERVER_ERROR))
            }
        }
    }
}
