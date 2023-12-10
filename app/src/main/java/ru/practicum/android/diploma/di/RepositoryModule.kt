package ru.practicum.android.diploma.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.practicum.android.diploma.core.data.network.NetworkClient
import ru.practicum.android.diploma.core.data.room.AppDatabase
import ru.practicum.android.diploma.core.data.room.dao.VacancyDao
import ru.practicum.android.diploma.core.domain.api.GetDataByIdRepo
import ru.practicum.android.diploma.core.domain.api.GetDataRepo
import ru.practicum.android.diploma.core.domain.api.SearchRepo
import ru.practicum.android.diploma.core.domain.models.VacancyDetails
import ru.practicum.android.diploma.favorites.data.FavoritesVacancyListRepositoryImpl
import ru.practicum.android.diploma.favorites.domain.api.FavoritesVacancyListRepository
import ru.practicum.android.diploma.filters.data.repository.IndustriesRepositoryImpl
import ru.practicum.android.diploma.filters.domain.model.Industry
import ru.practicum.android.diploma.search.data.repository.SearchVacanciesRepository
import ru.practicum.android.diploma.search.domain.model.VacancyInList
import ru.practicum.android.diploma.util.Resource
import ru.practicum.android.diploma.vacancydetails.data.DeleteVacancyRepositoryImpl
import ru.practicum.android.diploma.vacancydetails.data.GetByIdVacancyDetailsRepoImpl
import ru.practicum.android.diploma.vacancydetails.data.SaveVacancyRepositoryImpl
import ru.practicum.android.diploma.vacancydetails.domain.api.DeleteVacancyRepository
import ru.practicum.android.diploma.vacancydetails.domain.api.SaveVacancyRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    @Singleton
    fun provideGetByIdVacancyDetailsRepoImpl(
        networkClient: NetworkClient,
        vacancyDao: VacancyDao
    ): GetDataByIdRepo<Resource<VacancyDetails>> {
        return GetByIdVacancyDetailsRepoImpl(networkClient, vacancyDao)
    }

    @Provides
    @Singleton
    fun provideDeleteVacancyRepository(addDatabase: AppDatabase): DeleteVacancyRepository {
        return DeleteVacancyRepositoryImpl(addDatabase)
    }

    @Provides
    @Singleton
    fun provideIndustriesRepository(networkClient: NetworkClient): GetDataRepo<Resource<List<Industry>>> {
        return IndustriesRepositoryImpl(networkClient)
    }

    @Provides
    @Singleton
    fun provideFavoritesVacancyListRepository(appDatabase: AppDatabase): FavoritesVacancyListRepository {
        return FavoritesVacancyListRepositoryImpl(appDatabase)
    }

    @Provides
    @Singleton
    fun provideSaveVacancyRepository(database: AppDatabase): SaveVacancyRepository {
        return SaveVacancyRepositoryImpl(database)
    }

    @Provides
    @Singleton
    fun provideSearchRepository(): SearchRepo<VacancyInList> {
        return SearchVacanciesRepository()
    }
}
