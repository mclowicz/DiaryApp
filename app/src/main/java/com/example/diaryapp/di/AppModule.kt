package com.example.diaryapp.di

import android.app.Application
import android.content.Context
import com.example.diaryapp.connectivity.NetworkConnectivityObserver
import com.example.diaryapp.presentation.screens.auth.AuthenticationRepository
import com.example.diaryapp.presentation.screens.main.MainRepository
import com.example.diaryapp.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.realm.kotlin.mongodb.App
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideContext(
        app: Application
    ): Context = app.applicationContext

    @Provides
    @Singleton
    fun provideApp(): App = App.create(Constants.APP_ID)

    @Provides
    @Singleton
    fun provideMainRepository(app: App): MainRepository = MainRepository(app)

    @Provides
    @Singleton
    fun provideAuthenticationRepository(app: App): AuthenticationRepository = AuthenticationRepository(app)

    @Provides
    @Singleton
    fun provideNetworkConnectivityObserver(
        @ApplicationContext context: Context
    ): NetworkConnectivityObserver = NetworkConnectivityObserver(context = context)
}