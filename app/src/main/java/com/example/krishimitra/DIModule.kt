package com.example.krishimitra

import android.content.Context
import com.example.krishimitra.data.lang_manager.LanguageManager
import com.example.krishimitra.data.repo.RepoImpl
import com.example.krishimitra.domain.repo.Repo
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DIModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Provides
    @Singleton
    fun provideLanguageManager(
        @ApplicationContext context: Context
    ): LanguageManager {
        return LanguageManager(context)
    }

    @Provides
    @Singleton
    fun provideFusedLocationClient(
        @ApplicationContext context: Context
    ): FusedLocationProviderClient {
        return LocationServices.getFusedLocationProviderClient(context)
    }


    @Provides
    @Singleton
    fun provideRepo(
        languageManager: LanguageManager,
        @ApplicationContext context: Context,
        fusedLocationClient: FusedLocationProviderClient
    ): Repo{
        return RepoImpl(
            languageManager = languageManager,
            context = context,
            fusedLocationClient = fusedLocationClient
        )
    }
}