package com.example.krishimitra

import android.content.Context
import androidx.room.Room
import com.example.krishimitra.data.local.KrishiMitraDatabase
import com.example.krishimitra.data.local.dao.MandiPriceDao
import com.example.krishimitra.data.remote.MandiPriceApiService
import com.example.krishimitra.data.repo.RepoImpl
import com.example.krishimitra.data.repo.lang_manager.LanguageManager
import com.example.krishimitra.data.repo.location_manager.LocationManager
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
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DIModule {

    const val MANDI_PRICE_RETROFIT = "mandi_price"

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
        locationManager: LocationManager,
        mandiApiService: MandiPriceApiService,
        localDb: KrishiMitraDatabase
    ): Repo {
        return RepoImpl(
            languageManager = languageManager,
            locationManager = locationManager,
            mandiApiService = mandiApiService,
            localDb = localDb
        )
    }

    @Provides
    @Singleton
    fun provideLocationManager(
        @ApplicationContext context: Context,
        fusedLocationClient: FusedLocationProviderClient
    ): LocationManager {
        return LocationManager(
            context = context,
            fusedLocationClient = fusedLocationClient
        )
    }

    @Provides
    @Singleton
    @Named(MANDI_PRICE_RETROFIT)
    fun provideMandiPriceRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(MandiPriceApiService.mandi_base_url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideMandiPriceApi(
        @Named(MANDI_PRICE_RETROFIT) retrofit: Retrofit
    ): MandiPriceApiService {
        return retrofit.create(MandiPriceApiService::class.java)
    }


    @Provides
    @Singleton
    fun provideKrishiMitraDatabase(
        @ApplicationContext context: Context
    ): KrishiMitraDatabase {
        return Room.databaseBuilder(
            context = context,
            klass = KrishiMitraDatabase::class.java,
            name = "krishi_mitra_db"
        ).build()

    }


    @Provides
    @Singleton
    fun provideMandiPriceDao(
        krishiMitraDatabase: KrishiMitraDatabase
    ): MandiPriceDao {
        return krishiMitraDatabase.mandiPriceDao()
    }


}