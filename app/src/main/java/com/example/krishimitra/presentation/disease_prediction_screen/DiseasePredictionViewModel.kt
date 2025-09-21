package com.example.krishimitra.presentation.disease_prediction_screen

import android.content.Context
import android.net.Uri
import android.os.Build
import android.speech.tts.TextToSpeech
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    private var currentLang = mutableStateOf("eng")

    init {

        viewModelScope.launch(Dispatchers.IO) {
            repo.getLanguage().collect {
                currentLang.value = it
            }
        }
    }

    private val _state =
        MutableStateFlow(DiseasePredictionScreenState())
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
                    // Start with a clear introductory sentence
                    append("The predicted disease is ${event.predictedData.Disease}. ")

                    // Provide the description as a separate, full sentence
                    append("Here is the description: ${event.predictedData.Description}. ")

                    // Use a clear phrase for the precautions
                    append("The recommended precautions are: ${event.predictedData.Precautions.joinToString(", ")}. ")

                    // Use a clear phrase for the treatment
                    append("The recommended treatment is: ${event.predictedData.Treatment.joinToString(", ")}.")
                }
                initAndSpeak(context, textToSpeak)
            }

            is DiseasePredictionScreenEvent.onStopSpeak -> {

                stopSpeak()

            }
        }
    }

    private fun initAndSpeak(context: Context, text: String) {
        if (tts == null) {
            tts = TextToSpeech(context) { status ->
                if (status == TextToSpeech.SUCCESS) {
                    val result = tts?.setLanguage(getLocaleFromLangCode(currentLang.value))
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        viewModelScope.launch(Dispatchers.IO) {
                            _event.emit("Device doesn't support English TTS.")
                        }
                    } else {
                        isTtsInitialized = true
                        startSpeaking(text)
                    }
                }
            }
        } else if (isTtsInitialized) {
            if (isSpeaking) stopSpeak() else startSpeaking(text)
        } else {
            startSpeaking(text)
        }
    }


    private fun startSpeaking(text: String) {
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "tts1")
        isSpeaking = true
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


    private fun predictCropDisease(
        filePath: Uri
    ) {
        viewModelScope.launch(Dispatchers.IO) {

            repo.predictCropDisease(currentLang.value, filePath).collect { collect ->
                when (collect) {
                    is ResultState.Error<*> -> {
                        _event.emit(collect.exception)
                        _state.update {
                            it.copy(
                                isLoading = false
                            )
                        }
                    }

                    ResultState.Loading -> {
                        _state.update {
                            it.copy(
                                isLoading = true
                            )
                        }
                    }

                    is ResultState.Success -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                response = collect.data
                            )
                        }
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
                "eng" -> Locale("en", "IN") // Use a more appropriate locale
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