package ru.practicum.android.diploma.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.practicum.android.diploma.core.data.room.GtjDatabase

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    fun provideDatabase(@ApplicationContext context: Context): GtjDatabase =
        Room.databaseBuilder(context, GtjDatabase::class.java, "gtj_database.db").build()

}
