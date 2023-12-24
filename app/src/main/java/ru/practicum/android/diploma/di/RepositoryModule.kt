package ru.practicum.android.diploma.di

import android.content.SharedPreferences
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.practicum.android.diploma.core.data.network.NetworkClient
import ru.practicum.android.diploma.core.data.room.dao.VacancyDao
import ru.practicum.android.diploma.core.data.sharedprefs.FiltersRepository
import ru.practicum.android.diploma.core.data.sharedprefs.FiltersTempRepository
import ru.practicum.android.diploma.core.data.sharedprefs.RefreshSearchFlagRepository
import ru.practicum.android.diploma.core.domain.api.DeleteDataRepo
import ru.practicum.android.diploma.core.domain.api.GetDataByIdRepo
import ru.practicum.android.diploma.core.domain.api.GetDataRepo
import ru.practicum.android.diploma.core.domain.api.SaveDataRepo
import ru.practicum.android.diploma.core.domain.api.SearchRepo
import ru.practicum.android.diploma.core.domain.models.Filters
import ru.practicum.android.diploma.vacancydetails.domain.model.VacancyDetails
import ru.practicum.android.diploma.favorites.data.FavoritesVacancyListRepositoryImpl
import ru.practicum.android.diploma.filters.data.repository.CountriesRepositoryImpl
import ru.practicum.android.diploma.filters.data.repository.IndustriesRepositoryImpl
import ru.practicum.android.diploma.filters.data.repository.RegionsRepositoryImpl
import ru.practicum.android.diploma.filters.domain.model.Country
import ru.practicum.android.diploma.filters.domain.model.Industry
import ru.practicum.android.diploma.search.data.repository.SearchVacanciesRepository
import ru.practicum.android.diploma.search.domain.model.SearchResult
import ru.practicum.android.diploma.search.domain.model.Vacancy
import ru.practicum.android.diploma.util.Resource
import ru.practicum.android.diploma.vacancydetails.data.SimilarVacanciesRepoImpl
import ru.practicum.android.diploma.vacancydetails.data.VacancyRepositoryDb
import javax.inject.Named
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
    ): GetDataByIdRepo<Resource<List<Vacancy>>> {
        return SimilarVacanciesRepoImpl(networkClient)
    }

    @Provides
    @Singleton
    fun provideIndustriesRepository(networkClient: NetworkClient): GetDataRepo<Resource<List<Industry>>> {
        return IndustriesRepositoryImpl(networkClient)
    }

    @Provides
    @Singleton
    fun provideFavoritesVacancyListRepository(vacancyDao: VacancyDao): GetDataRepo<List<Vacancy>> {
        return FavoritesVacancyListRepositoryImpl(vacancyDao)
    }

    @Provides
    @Singleton
    fun provideSearchRepository(
        networkClient: NetworkClient,
        filtersRepo: FiltersRepository
    ): SearchRepo<SearchResult> {
        return SearchVacanciesRepository(networkClient, filtersRepo)
    }

    @Provides
    @Singleton
    @Named(COUNTRIES_REPOSITORY_IMPL)
    fun provideCountryRepository(networkClient: NetworkClient): GetDataRepo<Resource<List<Country>>> {
        return CountriesRepositoryImpl(networkClient)
    }

    @Provides
    @Singleton
    fun provideFiltersRepository(sharedPreferences: SharedPreferences, gson: Gson): FiltersRepository {
        return FiltersRepository(sharedPreferences, gson)
    }

    @Provides
    @Singleton
    @Named(FILTERS_GET_REPOSITORY)
    fun provideGetFiltersRepo(filtersRepo: FiltersRepository): GetDataRepo<Filters> {
        return filtersRepo
    }

    @Provides
    @Singleton
    @Named(FILTERS_SAVE_REPOSITORY)
    fun provideSaveFiltersRepo(filtersRepo: FiltersRepository): SaveDataRepo<Filters> {
        return filtersRepo
    }

    @Provides
    @Singleton
    fun provideFiltersTempRepository(sharedPreferences: SharedPreferences, gson: Gson): FiltersTempRepository {
        return FiltersTempRepository(sharedPreferences, gson)
    }

    @Provides
    @Singleton
    @Named(FILTERS_TEMP_SAVE_REPOSITORY)
    fun provideSaveFiltersTempRepo(filtersRepo: FiltersTempRepository): SaveDataRepo<Filters> {
        return filtersRepo
    }

    @Provides
    @Singleton
    @Named(FILTERS_TEMP_GET_REPOSITORY)
    fun provideGetFiltersTempRepo(filtersRepo: FiltersTempRepository): GetDataRepo<Filters> {
        return filtersRepo
    }

    @Provides
    @Singleton
    fun provideRegionsRepository(networkClient: NetworkClient): RegionsRepositoryImpl {
        return RegionsRepositoryImpl(networkClient)
    }

    @Provides
    @Singleton
    fun provideGetDataByIdRepo(regionsRepositoryImpl: RegionsRepositoryImpl): GetDataByIdRepo<Resource<List<Country>>> {
        return regionsRepositoryImpl
    }

    @Provides
    @Singleton
    @Named(REGIONS_REPOSITORY_IMPL)
    fun provideGetDataRepo(regionsRepositoryImpl: RegionsRepositoryImpl): GetDataRepo<Resource<List<Country>>> {
        return regionsRepositoryImpl
    }

    @Provides
    @Singleton
    fun provideRefreshSearchFlagRepo(sharedPreferences: SharedPreferences): RefreshSearchFlagRepository {
        return RefreshSearchFlagRepository(sharedPreferences)
    }

    @Provides
    @Singleton
    fun provideGetRefreshFlagRepo(refreshSearchFlagRepository: RefreshSearchFlagRepository): GetDataRepo<Boolean> {
        return refreshSearchFlagRepository
    }

    @Provides
    @Singleton
    fun provideSaveRefreshFlagRepo(refreshSearchFlagRepository: RefreshSearchFlagRepository): SaveDataRepo<Boolean> {
        return refreshSearchFlagRepository
    }

    companion object {
        const val COUNTRIES_REPOSITORY_IMPL = "CountriesRepositoryImpl"
        const val REGIONS_REPOSITORY_IMPL = "RegionsRepositoryImpl"
        const val FILTERS_SAVE_REPOSITORY = "FiltersSaveRepository"
        const val FILTERS_GET_REPOSITORY = "FiltersGetRepository"
        const val FILTERS_TEMP_SAVE_REPOSITORY = "FiltersTempSaveRepository"
        const val FILTERS_TEMP_GET_REPOSITORY = "FiltersTempGetRepository"
    }
}
