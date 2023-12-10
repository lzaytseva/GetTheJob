package ru.practicum.android.diploma.vacancydetails.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ru.practicum.android.diploma.core.data.dto.requests.VacancyDetailsSearchRequest
import ru.practicum.android.diploma.core.data.dto.responses.VacancyDetailsSearchResponse
import ru.practicum.android.diploma.core.data.network.NetworkClient
import ru.practicum.android.diploma.core.data.network.RetrofitNetworkClient.Companion.RC_NO_INTERNET
import ru.practicum.android.diploma.core.data.network.RetrofitNetworkClient.Companion.RC_OK
import ru.practicum.android.diploma.core.data.room.AppDatabase
import ru.practicum.android.diploma.core.data.room.VacancyEntityMapper
import ru.practicum.android.diploma.core.domain.models.VacancyDetails
import ru.practicum.android.diploma.util.Resource
import ru.practicum.android.diploma.vacancydetails.domain.api.VacancyDetailsRepository
import javax.inject.Inject

class VacancyDetailsRepositoryImpl @Inject constructor(
    private val networkClient: NetworkClient,
    private val appDatabase: AppDatabase,
) : VacancyDetailsRepository {

    override fun getVacancyDetailsById(vacancyId: String): Flow<Resource<VacancyDetails>> = flow {
        val vacancyDetailsDb = appDatabase.vacancyDao.getVacancyById(vacancyId)?.let { vacancyEntity ->
            VacancyEntityMapper.map(vacancyEntity)
        }
        if (vacancyDetailsDb != null) {
            emit(Resource.Success(vacancyDetailsDb))
            val response = networkClient.doRequest(VacancyDetailsSearchRequest(vacancyId))
            if (response.resultCode == RC_OK) {
                emit(
                    Resource.Success(
                        VacancyDetailsDtoMapper.map((response as VacancyDetailsSearchResponse).dto).apply {
                            isFavorite = true
                        }
                    )
                )
            }
        } else {
            val response = networkClient.doRequest(VacancyDetailsSearchRequest(vacancyId))
            when (response.resultCode) {
                RC_NO_INTERNET -> emit(Resource.Error("No internet"))

                RC_OK -> emit(
                    Resource.Success(VacancyDetailsDtoMapper.map((response as VacancyDetailsSearchResponse).dto))
                )

                else -> emit(Resource.Error("Server error"))
            }
        }
    }.flowOn(Dispatchers.IO)
}
