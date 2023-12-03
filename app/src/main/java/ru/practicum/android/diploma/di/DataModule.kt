package ru.practicum.android.diploma.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import ru.practicum.android.diploma.core.data.navigation.ExternalNavigatorImpl
import ru.practicum.android.diploma.core.data.network.HhApiService
import ru.practicum.android.diploma.core.data.network.NetworkClient
import ru.practicum.android.diploma.core.data.network.RetrofitNetworkClient
import ru.practicum.android.diploma.core.data.room.GtjDatabase
import ru.practicum.android.diploma.core.domain.api.ExternalNavigator

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    fun provideDatabase(@ApplicationContext context: Context): GtjDatabase =
        Room.databaseBuilder(context, GtjDatabase::class.java, "gtj_database.db").build()

    @Provides
    fun provideExternalNavigator(@ApplicationContext appContext: Context): ExternalNavigator {
        return ExternalNavigatorImpl(appContext)
    }

    @Provides
    fun provideHhService(): HhApiService {
        return Retrofit.Builder()
            .baseUrl(HH_BASE_URL)
            .build()
            .create(HhApiService::class.java)
    }

    @Provides
    fun provideRetrofitNetworkClient(@ApplicationContext context: Context, hhService: HhApiService): NetworkClient {
        return RetrofitNetworkClient(context, hhService)
    }

    companion object {
        private const val HH_BASE_URL = "https://api.hh.ru/"
    }
}
