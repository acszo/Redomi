package com.acszo.redomi.di

import android.content.Context
import com.acszo.redomi.data.SettingsDataStore
import com.acszo.redomi.repository.DataStoreRepository
import com.acszo.redomi.repository.GithubRepository
import com.acszo.redomi.repository.SongLinkRepository
import com.acszo.redomi.service.Api.BASE_URL_GITHUB
import com.acszo.redomi.service.Api.BASE_URL_SONG_LINK
import com.acszo.redomi.service.GithubService
import com.acszo.redomi.service.SongLinkService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun jsonConverter(): Converter.Factory {
        val contentType = "application/json".toMediaType()
        val json = Json { ignoreUnknownKeys = true }
        return json.asConverterFactory(contentType)
    }

    @Provides
    @Singleton
    fun provideSongLinkService(
        jsonConverter: Converter.Factory
    ): SongLinkService = Retrofit.Builder()
        .baseUrl(BASE_URL_SONG_LINK)
        .addConverterFactory(jsonConverter)
        .build()
        .create(SongLinkService::class.java)

    @Provides
    @Singleton
    fun provideGithubService(
        jsonConverter: Converter.Factory
    ): GithubService = Retrofit.Builder()
        .baseUrl(BASE_URL_GITHUB)
        .addConverterFactory(jsonConverter)
        .build()
        .create(GithubService::class.java)

    @Provides
    @Singleton
    fun provideSongRepository(
        songLinkService: SongLinkService
    ): SongLinkRepository = SongLinkRepository(songLinkService)

    @Provides
    @Singleton
    fun provideGithubRepository(
        githubService: GithubService
    ): GithubRepository = GithubRepository(githubService)

    @Provides
    @Singleton
    fun provideSettingsDataStore(
        @ApplicationContext context: Context
    ): SettingsDataStore = SettingsDataStore(context)

    @Provides
    @Singleton
    fun provideDataStoreRepository(
        @ApplicationContext context: Context
    ): DataStoreRepository = DataStoreRepository(context)

}