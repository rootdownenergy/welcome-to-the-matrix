package com.rootdown.dev.codingchallengeoneadidev.di

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.rootdown.dev.codingchallengeoneadidev.data.feature_character_explorer.net.ApiCharacterExplorer
import com.rootdown.dev.codingchallengeoneadidev.di.util.UtilSingleton
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
object NetworkModule {
    @Provides
    fun provideGson(): Gson = GsonBuilder().create()

    @Singleton
    @Provides
    @Named("api")
    fun provideRetrofitApi(gson: Gson, @ApplicationContext appContext: Context): Retrofit = Retrofit.Builder()
        .baseUrl(UtilSingleton.api)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    @Provides
    fun providesApiCharacterExplorer(@Named("api") retrofit: Retrofit): ApiCharacterExplorer = retrofit.create(ApiCharacterExplorer::class.java)

}