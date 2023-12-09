package ru.practicum.android.diploma.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.practicum.android.diploma.core.data.network.NetworkClient
import ru.practicum.android.diploma.core.data.room.AppDatabase
import ru.practicum.android.diploma.favorites.data.DeleteVacancyRepositoryImpl
import ru.practicum.android.diploma.favorites.domain.api.DeleteVacancyRepository
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
    fun provideDeleteVacancyRepository(addDatabase: AppDatabase): DeleteVacancyRepository {
        return DeleteVacancyRepositoryImpl(addDatabase)
    }
}
