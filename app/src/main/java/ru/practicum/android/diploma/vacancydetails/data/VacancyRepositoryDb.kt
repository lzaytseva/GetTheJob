package ru.practicum.android.diploma.vacancydetails.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ru.practicum.android.diploma.core.data.dto.requests.VacancyDetailsSearchRequest
import ru.practicum.android.diploma.core.data.dto.responses.VacancyDetailsSearchResponse
import ru.practicum.android.diploma.core.data.network.NetworkClient
import ru.practicum.android.diploma.core.data.network.RetrofitNetworkClient
import ru.practicum.android.diploma.core.data.room.VacancyEntityMapper
import ru.practicum.android.diploma.core.data.room.dao.VacancyDao
import ru.practicum.android.diploma.core.domain.api.DeleteDataRepo
import ru.practicum.android.diploma.core.domain.api.GetDataByIdRepo
import ru.practicum.android.diploma.core.domain.api.SaveDataRepo
import ru.practicum.android.diploma.core.domain.models.ErrorType
import ru.practicum.android.diploma.core.domain.models.VacancyDetails
import ru.practicum.android.diploma.util.Resource

class VacancyRepositoryDb(
    private val dao: VacancyDao,
    private val networkClient: NetworkClient
) : DeleteDataRepo<String>, SaveDataRepo<VacancyDetails>, GetDataByIdRepo<Resource<VacancyDetails>> {

    override suspend fun delete(data: String) {
        dao.deleteVacancy(data)
    }

    override fun getById(id: String): Flow<Resource<VacancyDetails>?> = flow {
        val vacancyFromDb = dao.getVacancyById(id)?.let { vacancyEntity ->
            VacancyEntityMapper.map(vacancyEntity)
        }
        if (vacancyFromDb != null) {
            val response = networkClient.doRequest(VacancyDetailsSearchRequest(id))
            if (response.resultCode == RetrofitNetworkClient.CODE_SUCCESS) {
                dao.updateVacancy(
                    VacancyEntityMapper.map(
                        VacancyDetailsDtoMapper.map((response as VacancyDetailsSearchResponse).dto)
                    )
                )
                emit(
                    Resource.Success(
                        VacancyDetailsDtoMapper.map(response.dto).apply {
                            isFavoriteWrapper.isFavorite = true
                        }
                    )
                )
            } else {
                emit(Resource.Success(vacancyFromDb))
            }
        } else {
            val response = networkClient.doRequest(VacancyDetailsSearchRequest(id))
            when (response.resultCode) {
                RetrofitNetworkClient.CODE_NO_INTERNET -> emit(Resource.Error(ErrorType.NO_INTERNET))

                RetrofitNetworkClient.CODE_SUCCESS -> emit(
                    Resource.Success(VacancyDetailsDtoMapper.map((response as VacancyDetailsSearchResponse).dto))
                )

                else -> emit(Resource.Error(ErrorType.SERVER_ERROR))
            }
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun save(data: VacancyDetails?) {
        data?.run {
            dao.saveVacancy(VacancyEntityMapper.map(this))
        }
    }

}
