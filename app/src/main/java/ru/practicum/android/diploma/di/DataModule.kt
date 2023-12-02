package ru.practicum.android.diploma.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

//    @Provides
//    fun provideDatabase(@ApplicationContext context: Context): GtjDatabase =
//        Room.databaseBuilder(context, GtjDatabase::class.java, "gtj_database").build()

}
