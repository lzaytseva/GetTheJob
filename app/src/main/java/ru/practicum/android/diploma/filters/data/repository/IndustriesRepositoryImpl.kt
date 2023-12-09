package ru.practicum.android.diploma.filters.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.core.data.network.NetworkClient
import ru.practicum.android.diploma.core.data.network.RetrofitNetworkClient
import ru.practicum.android.diploma.core.domain.api.GetDataRepo
import ru.practicum.android.diploma.filters.data.dto.IndustriesRequest
import ru.practicum.android.diploma.filters.data.dto.IndustriesResponse
import ru.practicum.android.diploma.filters.data.mapper.IndustryMapper
import ru.practicum.android.diploma.filters.domain.model.Industry
import ru.practicum.android.diploma.util.Resource
import javax.inject.Inject

class IndustriesRepositoryImpl @Inject constructor(
    private val networkClient: NetworkClient
) : GetDataRepo<Resource<List<Industry>>> {
    override fun get(): Flow<Resource<List<Industry>>?> = flow {
        val response = networkClient.doRequest(IndustriesRequest)

        when (response.resultCode) {
            RetrofitNetworkClient.RC_NO_INTERNET -> {
                // убрать ошибки в enum
                emit(Resource.Error("Нет интернета"))
            }

            RetrofitNetworkClient.RC_OK -> {
                emit(Resource.Success(
                    IndustryMapper.mapDtoToEntityList(
                        (response as IndustriesResponse).industries
                    ).sortedBy { industry ->
                        industry.name
                    }
                ))
            }

            else -> {
                // убрать ошибки в enum
                emit(Resource.Error("Ошибка сервера"))
            }
        }
    }
}
