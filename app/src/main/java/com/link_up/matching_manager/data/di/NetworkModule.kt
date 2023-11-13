package com.link_up.matching_manager.data.di

import com.link_up.matching_manager.data.remote.ArenaRemoteDataSource
import com.link_up.matching_manager.data.retrofit.AuthorizationInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton


@Module
class NetworkModule {

    private val BASE_URL = "https://dapi.kakao.com"

    private val okHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(AuthorizationInterceptor())
            .build()
    }

    @Singleton
    @Provides
    fun providesRetrofit() : Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun providersArenaRemoteDataSource(retrofit: Retrofit) : ArenaRemoteDataSource{
        return retrofit.create(ArenaRemoteDataSource::class.java)
    }
}