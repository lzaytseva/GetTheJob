package ru.practicum.android.diploma.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.practicum.android.diploma.core.data.network.NetworkClient
import ru.practicum.android.diploma.core.domain.api.SearchRepo
import ru.practicum.android.diploma.search.data.repository.SearchVacanciesRepository
import ru.practicum.android.diploma.search.domain.model.VacancyInList
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
    fun provideSearchRepository(): SearchRepo<VacancyInList> {
        return SearchVacanciesRepository()
    }
}
