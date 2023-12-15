package ru.practicum.android.diploma.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.practicum.android.diploma.core.data.network.NetworkClient
import ru.practicum.android.diploma.core.data.room.AppDatabase
import ru.practicum.android.diploma.core.data.room.dao.VacancyDao
import ru.practicum.android.diploma.core.domain.api.DeleteDataRepo
import ru.practicum.android.diploma.core.domain.api.GetDataByIdRepo
import ru.practicum.android.diploma.core.domain.api.GetDataRepo
import ru.practicum.android.diploma.core.domain.api.SaveDataRepo
import ru.practicum.android.diploma.core.domain.api.SearchRepo
import ru.practicum.android.diploma.core.domain.models.VacancyDetails
import ru.practicum.android.diploma.favorites.data.FavoritesVacancyListRepositoryImpl
import ru.practicum.android.diploma.favorites.domain.api.FavoritesVacancyListRepository
import ru.practicum.android.diploma.filters.data.repository.CountriesRepositoryImpl
import ru.practicum.android.diploma.filters.data.repository.IndustriesRepositoryImpl
import ru.practicum.android.diploma.filters.domain.model.Country
import ru.practicum.android.diploma.filters.domain.model.Industry
import ru.practicum.android.diploma.search.data.repository.SearchVacanciesRepository
import ru.practicum.android.diploma.search.domain.model.SearchResult
import ru.practicum.android.diploma.search.domain.model.VacancyInList
import ru.practicum.android.diploma.util.Resource
import ru.practicum.android.diploma.vacancydetails.data.SimilarVacanciesRepoImpl
import ru.practicum.android.diploma.vacancydetails.data.VacancyRepositoryDb
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    @Singleton
    fun provideVacancyRepositoryDb(dao: VacancyDao, networkClient: NetworkClient): VacancyRepositoryDb {
        return VacancyRepositoryDb(dao, networkClient)
    }

    @Provides
    @Singleton
    fun provideVacancyGetById(vacancyRepo: VacancyRepositoryDb): GetDataByIdRepo<Resource<VacancyDetails>> {
        return vacancyRepo
    }

    @Provides
    @Singleton
    fun provideVacancySaveRepo(vacancyRepo: VacancyRepositoryDb): SaveDataRepo<VacancyDetails> {
        return vacancyRepo
    }

    @Provides
    @Singleton
    fun provideVacancyDeleteRepo(vacancyRepo: VacancyRepositoryDb): DeleteDataRepo<String> {
        return vacancyRepo
    }

    @Provides
    @Singleton
    fun provideSimilarVacanciesRepoImpl(
        networkClient: NetworkClient,
    ): GetDataByIdRepo<Resource<List<VacancyInList>>> {
        return SimilarVacanciesRepoImpl(networkClient)
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
    fun provideSearchRepository(networkClient: NetworkClient): SearchRepo<SearchResult> {
        return SearchVacanciesRepository(networkClient)
    }

    @Provides
    @Singleton
    fun provideCountryRepository(networkClient: NetworkClient): GetDataRepo<Resource<List<Country>>> {
        return CountriesRepositoryImpl(networkClient)
    }
}
