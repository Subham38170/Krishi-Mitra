package com.example.krishimitra

import android.content.Context
import androidx.compose.ui.res.stringResource
import androidx.datastore.preferences.preferencesDataStore


data class Language(
    val code: String,
    val englishName: String,
    val nativeName: String
)

object Constants {

    val INDIAN_STATES = listOf(
        "Andhra Pradesh",
        "Arunachal Pradesh",
        "Assam",
        "Bihar",
        "Chhattisgarh",
        "Goa",
        "Gujarat",
        "Haryana",
        "Himachal Pradesh",
        "Jharkhand",
        "Karnataka",
        "Kerala",
        "Madhya Pradesh",
        "Maharashtra",
        "Manipur",
        "Meghalaya",
        "Mizoram",
        "Nagaland",
        "Odisha",
        "Punjab",
        "Rajasthan",
        "Sikkim",
        "Tamil Nadu",
        "Telangana",
        "Tripura",
        "Uttar Pradesh",
        "Uttarakhand",
        "West Bengal"
    )
    val SUPPORTED_LANGUAGES = listOf(
        Language("eng", "English", "English"),
        Language("as", "Assamese", "অসমীয়া"),
        Language("bh", "Bihari", "भोजपुरी/मैथिली/मगही"),
        Language("bn", "Bengali", "বাংলা"),
        Language("gu", "Gujarati", "ગુજરાતી"),
        Language("hi", "Hindi", "हिन्दी"),
        Language("him", "Himalayan", "हिमालयन"),
        Language("kn", "Kannada", "ಕನ್ನಡ"),
        Language("ks", "Kashmiri", "कश्मीरी"),
        Language("ml", "Malayalam", "മലയാളം"),
        Language("mni", "Manipuri", "মণিপুরী"),
        Language("mr", "Marathi", "मराठी"),
        Language("or", "Odia", "ଓଡ଼ିଆ"),
        Language("pa", "Punjabi", "ਪੰਜਾਬੀ"),
        Language("raj", "Rajasthani", "राजस्थानी"),
        Language("sa", "Sanskrit", "संस्कृतम्"),
        Language("sat", "Santali", "ᱥᱟᱱᱛᱟᱲᱤ"),
        Language("sd", "Sindhi", "سنڌي"),
        Language("ta", "Tamil", "தமிழ்"),
        Language("te", "Telugu", "తెలుగు"),
        Language("ur", "Urdu", "اردو")
    )


    val Context.dataStore by preferencesDataStore(name = "settings")


    const val MANDI_API_KEY = BuildConfig.MANDI_API_KEY

}

