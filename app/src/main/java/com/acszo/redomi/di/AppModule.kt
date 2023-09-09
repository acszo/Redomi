package com.acszo.redomi.di

import android.content.Context
import com.acszo.redomi.repository.DataStoreRepository
import com.acszo.redomi.repository.SongRepository
import com.acszo.redomi.service.Api
import com.acszo.redomi.service.SongService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(Api.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideSongService(retrofit: Retrofit): SongService = retrofit.create(SongService::class.java)

    @Provides
    @Singleton
    fun provideDataStoreRepository(
        @ApplicationContext context: Context
    ): DataStoreRepository = DataStoreRepository(context)

    @Provides
    @Singleton
    fun provideSongRepository(
        songService: SongService
    ): SongRepository = SongRepository(songService)

}