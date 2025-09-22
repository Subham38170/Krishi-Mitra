package com.example.krishimitra

import android.content.Context
import androidx.room.Room
import com.example.krishimitra.data.local.KrishiMitraDatabase
import com.example.krishimitra.data.local.dao.MandiPriceDao
import com.example.krishimitra.data.local.dao.WeatherDao
import com.example.krishimitra.data.remote.CropDiseasePredictionApiService
import com.example.krishimitra.data.remote.MandiPriceApiService
import com.example.krishimitra.data.remote.WeatherApiService
import com.example.krishimitra.data.remote_meidator.WeatherRemoteMediator
import com.example.krishimitra.data.repo.DataStoreManager
import com.example.krishimitra.data.repo.NetworkConnectivityObserverImpl
import com.example.krishimitra.data.repo.RepoImpl
import com.example.krishimitra.data.repo.LanguageManager
import com.example.krishimitra.data.repo.LocationManager
import com.example.krishimitra.domain.repo.NetworkConnectivityObserver
import com.example.krishimitra.domain.repo.Repo
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestoreSettings
import com.google.firebase.firestore.memoryCacheSettings
import com.google.firebase.firestore.persistentCacheSettings
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DIModule {

    const val MANDI_PRICE_RETROFIT = "mandi_price"
    const val DISEASE_PREDICTION_RETROFIT = "disease_prediction"
    const val WEATHER_DATA_RETROFIT = "weather_data"

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore {
        val settings = firestoreSettings {
            setLocalCacheSettings(memoryCacheSettings {})
            setLocalCacheSettings(persistentCacheSettings {})
        }
        val firestore = FirebaseFirestore.getInstance()
        firestore.firestoreSettings = settings
        return firestore
    }

    @Provides
    @Singleton
    fun provideFirebaseStorage(): FirebaseStorage {
        return FirebaseStorage.getInstance()
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
        localDb: KrishiMitraDatabase,
        dataStoreManager: DataStoreManager,
        firestoreDb: FirebaseFirestore,
        diseasePredictionApiService: CropDiseasePredictionApiService,
        firebaseStorage: FirebaseStorage,
        firebaseAuth: FirebaseAuth,
        weatherApiService: WeatherApiService,
        weatherRemoteMediator: WeatherRemoteMediator,
        @ApplicationContext context: Context
    ): Repo {
        return RepoImpl(
            languageManager = languageManager,
            locationManager = locationManager,
            mandiApiService = mandiApiService,
            localDb = localDb,
            dataStoreManager = dataStoreManager,
            firestoreDb = firestoreDb,
            diseasePredictionApiService = diseasePredictionApiService,
            context = context,
            firebaseStorage = firebaseStorage,
            firebaseAuth = firebaseAuth,
            weatherApiService = weatherApiService,
            weatherRemoteMediator = weatherRemoteMediator
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
    @Named(DISEASE_PREDICTION_RETROFIT)
    fun provideDiseasePredictionRetrofit(): Retrofit {
        return Retrofit
            .Builder()
            .baseUrl("https://609daac8c29c.ngrok-free.app/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }


    @Provides
    @Singleton
    fun provideCropDiseasePredictionApi(
        @Named(DISEASE_PREDICTION_RETROFIT) retrofit: Retrofit
    ): CropDiseasePredictionApiService {
        return retrofit.create(CropDiseasePredictionApiService::class.java)
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
    @Named(WEATHER_DATA_RETROFIT)
    fun provideWeatherDataApi(

    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideWeatherApi(
        @Named(WEATHER_DATA_RETROFIT) retrofit: Retrofit
    ): WeatherApiService {
        return retrofit.create(WeatherApiService::class.java)
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


    @Provides
    @Singleton
    fun provideDataStoreManager(
        @ApplicationContext context: Context
    ): DataStoreManager {
        return DataStoreManager(context)
    }


    @Provides
    @Singleton
    fun provideWeatherDao(
        krishiMitraDatabase: KrishiMitraDatabase
    ): WeatherDao {
        return krishiMitraDatabase.weatherDataDao()
    }

    @Provides
    @Singleton
    fun provideWeatherRemoteMediator(
        weatherApi: WeatherApiService,
        weaterDao: WeatherDao,
        networkConnectivityObserver: NetworkConnectivityObserver

    ): WeatherRemoteMediator {
        return WeatherRemoteMediator(
            api = weatherApi,
            dao = weaterDao,
            networkObserver = networkConnectivityObserver
        )
    }


    @Provides
    @Singleton
    fun provideNetworkConnectivityObserver(
        @ApplicationContext context: Context
    ): NetworkConnectivityObserver{
        return NetworkConnectivityObserverImpl(
            context = context,
            scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
        )
    }



}