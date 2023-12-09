package ru.practicum.android.diploma.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.practicum.android.diploma.core.data.network.NetworkClient
import ru.practicum.android.diploma.core.data.room.AppDatabase
import ru.practicum.android.diploma.core.domain.api.GetDataRepo
import ru.practicum.android.diploma.filters.data.repository.IndustriesRepositoryImpl
import ru.practicum.android.diploma.filters.domain.model.Industry
import ru.practicum.android.diploma.util.Resource
import ru.practicum.android.diploma.vacancydetails.data.VacancyDetailsRepositoryImpl
import ru.practicum.android.diploma.vacancydetails.domain.api.VacancyDetailsRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    @Singleton
    fun provideVacancyDetailsRepository(networkClient: NetworkClient): VacancyDetailsRepository {
        return VacancyDetailsRepositoryImpl(networkClient)
    }

    @Provides
    @Singleton
    fun provideIndustriesRepository(networkClient: NetworkClient): GetDataRepo<Resource<List<Industry>>> {
        return IndustriesRepositoryImpl(networkClient)
    }

    @Provides
    @Singleton
    fun provideVacancyDetailsRepository(appDatabase: AppDatabase): VacancyDetailsRepository {
        return VacancyDetailsRepositoryImpl(appDatabase)
    }
}
