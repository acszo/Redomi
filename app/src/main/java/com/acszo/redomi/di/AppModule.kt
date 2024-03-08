package com.acszo.redomi.di

import android.content.Context
import com.acszo.redomi.data.SettingsDataStore
import com.acszo.redomi.repository.DataStoreRepository
import com.acszo.redomi.repository.GithubRepository
import com.acszo.redomi.repository.SongRepository
import com.acszo.redomi.service.Api
import com.acszo.redomi.service.GithubService
import com.acszo.redomi.service.SongService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    @Named("SongLink")
    fun provideSongLinkRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(Api.BASE_URL_SONG_LINK)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    @Named("Github")
    fun provideGithubRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(Api.BASE_URL_GITHUB)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideSongService(@Named("SongLink") retrofit: Retrofit): SongService =
        retrofit.create(SongService::class.java)

    @Provides
    @Singleton
    fun provideGithubService(@Named("Github") retrofit: Retrofit): GithubService =
        retrofit.create(GithubService::class.java)

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

    @Provides
    @Singleton
    fun provideSongRepository(
        songService: SongService
    ): SongRepository = SongRepository(songService)

    @Provides
    @Singleton
    fun provideGithubRepository(
        githubService: GithubService
    ): GithubRepository = GithubRepository(githubService)

}