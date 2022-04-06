package com.example.dndmonstereditor.di


import com.example.dndmonstereditor.rest.MonsterRepository
import com.example.dndmonstereditor.rest.MonsterRepositoryImpl
import com.example.dndmonstereditor.rest.Service
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


//dependency injection providers

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule() {

    @Provides
    @Singleton
    fun providesNetworkService(okHttpClient: OkHttpClient) =
        Retrofit.Builder()
            .baseUrl(Service.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(Service::class.java)

    @Provides
    @Singleton
    fun providesOkHttpClient() =
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()

    @Provides
    fun providesMonsterRepository(monsterService: Service): MonsterRepository =
        MonsterRepositoryImpl(monsterService)

    @Provides
    fun providesDispatcher(): CoroutineDispatcher = Dispatchers.IO

}