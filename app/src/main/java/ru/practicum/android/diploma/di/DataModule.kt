package ru.practicum.android.diploma.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.practicum.android.diploma.BuildConfig
import ru.practicum.android.diploma.core.data.navigation.ExternalNavigatorImpl
import ru.practicum.android.diploma.core.data.network.HhApiService
import ru.practicum.android.diploma.core.data.network.NetworkClient
import ru.practicum.android.diploma.core.data.network.RetrofitNetworkClient
import ru.practicum.android.diploma.core.data.room.AppDatabase
import ru.practicum.android.diploma.core.data.room.dao.VacancyDao
import ru.practicum.android.diploma.core.domain.api.ExternalNavigator
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "gtj_database.db").build()

    @Provides
    @Singleton
    fun provideVacancyDao(appDatabase: AppDatabase): VacancyDao =
        appDatabase.vacancyDao

    @Provides
    @Singleton
    fun provideExternalNavigator(@ApplicationContext appContext: Context): ExternalNavigator {
        return ExternalNavigatorImpl(appContext)
    }

    @Provides
    @Singleton
    fun provideHhService(): HhApiService {
        val client = OkHttpClient()
            .newBuilder()
            .addInterceptor { chain ->
                chain.run {
                    proceed(
                        request()
                            .newBuilder()
                            .addHeader("Authorization", "Bearer ${BuildConfig.HH_ACCESS_TOKEN}")
                            .addHeader("HH-User-Agent", "GetTheJob (lvzaytseva1@gmail.com)")
                            .build()
                    )
                }
            }
            .build()

        return Retrofit.Builder()
            .baseUrl(HH_BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(HhApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideRetrofitNetworkClient(@ApplicationContext context: Context, hhService: HhApiService): NetworkClient {
        return RetrofitNetworkClient(context, hhService)
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(GET_THE_JOB, Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return Gson()
    }

    companion object {
        private const val HH_BASE_URL = "https://api.hh.ru/"
        private const val GET_THE_JOB = "GetTheJobKey"
    }
}
