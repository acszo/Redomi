package com.acszo.redomi.di

import android.content.Context
import com.acszo.redomi.data.SettingsDataStore
import com.acszo.redomi.repository.GithubRepository
import com.acszo.redomi.repository.QobuzRepository
import com.acszo.redomi.repository.SongLinkRepository
import com.acszo.redomi.service.Api.BASE_URL_GITHUB
import com.acszo.redomi.service.Api.BASE_URL_SONG_LINK
import com.acszo.redomi.service.Api.BASE_URL_QOBUZ
import com.acszo.redomi.service.Api.BASE_URL_DEEZER
import com.acszo.redomi.service.DeezerService
import com.acszo.redomi.service.GithubService
import com.acszo.redomi.service.SongLinkService
import com.acszo.redomi.service.QobuzService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton
import java.util.concurrent.TimeUnit

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
        songLinkService: SongLinkService,
        qobuzRepository: QobuzRepository,
        deezerService: DeezerService
    ): SongLinkRepository = SongLinkRepository(songLinkService, qobuzRepository, deezerService)

    @Provides
    @Singleton
    fun provideCatalogClient(): OkHttpClient = OkHttpClient.Builder()
        .callTimeout(15, TimeUnit.SECONDS)
        .build()

    @Provides
    @Singleton
    fun provideQobuzService(jsonConverter: Converter.Factory, client: OkHttpClient): QobuzService =
        Retrofit.Builder()
            .baseUrl(BASE_URL_QOBUZ)
            .client(client)
            .addConverterFactory(jsonConverter)
            .build()
            .create(QobuzService::class.java)

    @Provides
    @Singleton
    fun provideDeezerService(jsonConverter: Converter.Factory, client: OkHttpClient): DeezerService =
        Retrofit.Builder()
            .baseUrl(BASE_URL_DEEZER)
            .client(client)
            .addConverterFactory(jsonConverter)
            .build()
            .create(DeezerService::class.java)

    @Provides
    @Singleton
    fun provideQobuzRepository(qobuzService: QobuzService): QobuzRepository =
        QobuzRepository(qobuzService)

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

}