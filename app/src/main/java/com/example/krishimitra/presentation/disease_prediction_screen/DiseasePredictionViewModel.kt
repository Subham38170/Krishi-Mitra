package com.example.krishimitra.presentation.disease_prediction_screen

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.krishimitra.R
import com.example.krishimitra.domain.ResultState
import com.example.krishimitra.domain.repo.Repo
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class DiseasePredictionViewModel @Inject constructor(
    private val repo: Repo,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private var tts: TextToSpeech? = null
    private var isTtsInitialized = false
    private var isSpeaking = false
    private var currentLangCode: String = "eng"

    init {

        initTts()
        viewModelScope.launch(Dispatchers.IO) {
            repo.getLanguage().collect { langCode ->
                currentLangCode = langCode
            }
        }
    }

    private val _state = MutableStateFlow(DiseasePredictionScreenState())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<String>()
    val event = _event.asSharedFlow()

    fun onEvent(event: DiseasePredictionScreenEvent) {
        when (event) {
            is DiseasePredictionScreenEvent.PredictCropDisease -> {
                predictCropDisease(event.imageUrl)
            }

            is DiseasePredictionScreenEvent.onSpeak -> {
                val textToSpeak = buildString {
                    append("${context.getString(R.string.the_predicted_disease_is)} ${event.predictedData.Disease}. ")
                    append("${context.getString(R.string.here_is_description)}: ${event.predictedData.Description}. ")
                    append(
                        "${context.getString(R.string.recommended_precautions_are)}: ${
                            event.predictedData.Precautions.joinToString(
                                ", "
                            )
                        }. "
                    )
                    append(
                        "${context.getString(R.string.recommended_treatment_is)}: ${
                            event.predictedData.Treatment.joinToString(
                                ", "
                            )
                        }."
                    )
                }
                handleSpeak(textToSpeak)
            }

            is DiseasePredictionScreenEvent.onStopSpeak -> {
                stopSpeak()
            }
        }
    }

    private fun initTts() {
        if (tts != null) {
            tts?.stop()
            tts?.shutdown()
            tts = null
        }

        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val locale = getLocaleFromLangCode(currentLangCode)
                val result = tts?.setLanguage(locale)

                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    // Launch an Intent to prompt the user to download the language data
                    val installIntent = Intent()
                    installIntent.action = TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA
                    installIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    try {
                        context.startActivity(installIntent)
                        viewModelScope.launch {
                            _event.emit("Language '$currentLangCode' voice data is missing. Please download it.")
                        }
                    } catch (e: Exception) {
                        Log.e("TTS", "Failed to start TTS data installation activity.", e)
                        viewModelScope.launch {
                            _event.emit("Cannot download voice data for '$currentLangCode'. Please check your device settings.")
                        }
                    }
                    isTtsInitialized = false
                } else {
                    isTtsInitialized = true
                    Log.d("TTS", "TTS engine initialized for language: $currentLangCode")
                }
            } else {
                isTtsInitialized = false
                viewModelScope.launch {
                    _event.emit("TTS initialization failed with status: $status")
                }
            }
        }
    }


    // Simplified function to handle the speaking request
    private fun handleSpeak(text: String) {
        if (isSpeaking) {
            stopSpeak()
        } else {
            startSpeaking(text)
        }
    }

    private fun startSpeaking(text: String) {
        if (isTtsInitialized) {
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "tts1")
            isSpeaking = true
        } else {
            viewModelScope.launch {
                _event.emit("TTS engine is not ready. Please wait.")
            }
        }
    }

    private fun stopSpeak() {
        tts?.stop()
        isSpeaking = false
    }


    override fun onCleared() {
        super.onCleared()
        tts?.stop()
        tts?.shutdown()
    }


    private fun predictCropDisease(filePath: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.predictCropDisease(currentLangCode, filePath).collect { collect ->
                when (collect) {
                    is ResultState.Error -> {
                        _event.emit(collect.exception)
                        _state.update { it.copy(isLoading = false) }
                    }

                    ResultState.Loading -> {
                        _state.update { it.copy(isLoading = true) }
                    }

                    is ResultState.Success -> {
                        _state.update { it.copy(isLoading = false, response = collect.data) }
                    }
                }
            }
        }
    }

    private fun getLocaleFromLangCode(code: String): Locale {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when (code) {
                "eng" -> Locale.forLanguageTag("en-IN") // Use a more appropriate locale
                "bn" -> Locale.forLanguageTag("bn-IN")
                "gu" -> Locale.forLanguageTag("gu-IN")
                "hi" -> Locale.forLanguageTag("hi-IN")
                "kn" -> Locale.forLanguageTag("kn-IN")
                "ml" -> Locale.forLanguageTag("ml-IN")
                "mr" -> Locale.forLanguageTag("mr-IN")
                "or" -> Locale.forLanguageTag("or-IN")
                "pa" -> Locale.forLanguageTag("pa-IN")
                "ta" -> Locale.forLanguageTag("ta-IN")
                "te" -> Locale.forLanguageTag("te-IN")
                "ur" -> Locale.forLanguageTag("ur-IN")
                else -> Locale.forLanguageTag("en-IN")
            }
        } else {
            when (code) {
                "eng" -> Locale("en", "IN")
                "bn" -> Locale("bn", "IN")
                "gu" -> Locale("gu", "IN")
                "hi" -> Locale("hi", "IN")
                "kn" -> Locale("kn", "IN")
                "ml" -> Locale("ml", "IN")
                "mr" -> Locale("mr", "IN")
                "or" -> Locale("or", "IN")
                "pa" -> Locale("pa", "IN")
                "ta" -> Locale("ta", "IN")
                "te" -> Locale("te", "IN")
                "ur" -> Locale("ur", "IN")
                else -> Locale("en", "IN")
            }
        }
    }


}