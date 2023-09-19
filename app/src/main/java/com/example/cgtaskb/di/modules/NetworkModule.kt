package com.example.cgtaskb.di.modules

import com.example.cgtaskb.BuildConfig
import com.example.cgtaskb.data.remote.api.APIService
import com.example.cgtaskb.data.repository.WeatherRepositoryImpl
import com.example.cgtaskb.domain.repository.WeatherRepository
import com.example.cgtaskb.domain.usecase.GetWeatherUseCase
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class NetworkModule {

    @Provides
    fun provideOkHttpClient(interceptor: HttpLoggingInterceptor): OkHttpClient =
        OkHttpClient.Builder().addInterceptor(interceptor)
            .addNetworkInterceptor(StethoInterceptor())
            .build()

    @Provides
    fun provideLoggingInterceptor() =
        HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

    @Provides
    @Singleton
    fun provideGson(): Gson = Gson()

    @Provides
    @Singleton
    fun provideGsonConverterFactory(gson: Gson): GsonConverterFactory =
        GsonConverterFactory.create(gson)

    @Singleton
    @Provides
    fun provideAPIService(
        okhttpClient: OkHttpClient,
        converterFactory: GsonConverterFactory
    ) = provideService(okhttpClient, converterFactory, APIService::class.java)

    private fun <T> provideService(
        okhttpClient: OkHttpClient,
        converterFactory: GsonConverterFactory, clazz: Class<T>
    ): T {
        return provideRetrofitInstance(okhttpClient, converterFactory).create(clazz)
    }

    private fun provideRetrofitInstance(
        okhttpClient: OkHttpClient,
        converterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okhttpClient)
            .addConverterFactory(converterFactory)
            .build()
    }

    @Provides
    @Singleton
    fun provideWeatherRepository(
        apiService: APIService,
    ) = WeatherRepositoryImpl(apiService) as WeatherRepository

    @Singleton
    @Provides
    fun provideWeatherUseCase(
        movieRepository: WeatherRepository
    ): GetWeatherUseCase {
        return GetWeatherUseCase(movieRepository)
    }

}