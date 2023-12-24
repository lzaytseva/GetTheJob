package ru.practicum.android.diploma.filters.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.core.data.network.NetworkClient
import ru.practicum.android.diploma.core.data.network.RetrofitNetworkClient
import ru.practicum.android.diploma.core.domain.api.GetDataRepo
import ru.practicum.android.diploma.core.domain.models.ErrorType
import ru.practicum.android.diploma.filters.data.dto.IndustriesRequest
import ru.practicum.android.diploma.filters.data.dto.IndustriesResponse
import ru.practicum.android.diploma.filters.data.mapper.IndustryMapper
import ru.practicum.android.diploma.filters.domain.model.Industry
import ru.practicum.android.diploma.util.Resource

class IndustriesRepositoryImpl(
    private val networkClient: NetworkClient
) : GetDataRepo<Resource<List<Industry>>> {
    override fun get(): Flow<Resource<List<Industry>>?> = flow {
        val response = networkClient.doRequest(IndustriesRequest)

        when (response.resultCode) {
            RetrofitNetworkClient.CODE_NO_INTERNET -> {
                emit(Resource.Error(ErrorType.NO_INTERNET))
            }

            RetrofitNetworkClient.CODE_SUCCESS -> {
                emit(Resource.Success(
                    IndustryMapper.mapDtoToEntityList(
                        (response as IndustriesResponse).industries
                    ).sortedBy { industry ->
                        industry.name
                    }
                ))
            }

            else -> {
                emit(Resource.Error(ErrorType.SERVER_ERROR))
            }
        }
    }
}
