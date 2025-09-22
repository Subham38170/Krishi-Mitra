package com.example.krishimitra.presentation.assistant_screen

import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.GenerativeBackend
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject



enum class AssistantState {
    IDLE, LISTENING, PROCESSING, SPEAKING, ERROR
}

// Data class to hold the UI state.
data class AssistantScreenState(
    val state: AssistantState = AssistantState.IDLE,
    val spokenText: String = "",
    val assistantResponse: String = "",
    val error: String? = null
)
@HiltViewModel
class AssistantScreenViewModel @Inject constructor(

): ViewModel() {


    private val model = lazy {
        Firebase.ai(backend = GenerativeBackend.googleAI())
            .generativeModel("gemini-2.5-flash-preview")
    }

    private lateinit var tts: TextToSpeech
    private lateinit var speechRecognizer: SpeechRecognizer
    private lateinit var speechRecognizerIntent: Intent

    // StateFlow to manage UI state in a thread-safe way.
    private val _uiState = MutableStateFlow(AssistantScreenState())
    val uiState = _uiState.asStateFlow()

    fun setup(tts: TextToSpeech, speechRecognizer: SpeechRecognizer) {
        this.tts = tts
        this.speechRecognizer = speechRecognizer
        setupSpeechRecognizer()
    }

    // Set up the speech recognizer for Hindi language.
    private fun setupSpeechRecognizer() {
        speechRecognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "hi-IN")
            putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak in Hindi...")
            putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, "com.example.krishimitra")
        }

        speechRecognizer.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {
                _uiState.value = _uiState.value.copy(state = AssistantState.LISTENING)
            }

            override fun onBeginningOfSpeech() {}
            override fun onRmsChanged(rmsdB: Float) {}
            override fun onBufferReceived(buffer: ByteArray?) {}
            override fun onEndOfSpeech() {}
            override fun onError(error: Int) {
                val errorMessage = when (error) {
                    SpeechRecognizer.ERROR_AUDIO -> "Audio recording error"
                    SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "Network timeout"
                    SpeechRecognizer.ERROR_NETWORK -> "Network error"
                    SpeechRecognizer.ERROR_NO_MATCH -> "No speech recognized"
                    SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "No input detected"
                    else -> "Speech recognizer error"
                }
                _uiState.value = _uiState.value.copy(state = AssistantState.ERROR, error = errorMessage)
            }

            override fun onResults(results: Bundle?) {
                val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                if (!matches.isNullOrEmpty()) {
                    val transcribedText = matches[0]
                    _uiState.value = _uiState.value.copy(spokenText = transcribedText)
                    processTextWithGemini(transcribedText)
                } else {
                    _uiState.value = _uiState.value.copy(state = AssistantState.IDLE, error = "No speech recognized.")
                }
            }
            override fun onPartialResults(partialResults: Bundle?) {}
            override fun onEvent(eventType: Int, params: Bundle?) {}
        })
    }

    fun startListening() {
        _uiState.value = _uiState.value.copy(state = AssistantState.LISTENING)
        speechRecognizer.startListening(speechRecognizerIntent)
    }

    private fun processTextWithGemini(text: String) {
        _uiState.value = _uiState.value.copy(state = AssistantState.PROCESSING)
        viewModelScope.launch {
            try {
                // Use a proper prompt here based on your application's purpose
                val response = model.value.generateContent(text)
                val assistantResponse = response.text ?: "Could not get a response from the assistant."
                _uiState.value = _uiState.value.copy(
                    state = AssistantState.SPEAKING,
                    assistantResponse = assistantResponse
                )
                speakResponse(assistantResponse)
            } catch (e: Exception) {
                Log.e("AssistantScreenViewModel", "Error generating content: ${e.message}")
                _uiState.value = _uiState.value.copy(state = AssistantState.ERROR, error = "Error: ${e.message}")
            }
        }
    }

    // Use the TTS engine to speak the given text.
    private fun speakResponse(text: String) {
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
        tts.setOnUtteranceProgressListener(object : android.speech.tts.UtteranceProgressListener() {
            override fun onStart(utteranceId: String?) {}
            override fun onDone(utteranceId: String?) {
                _uiState.value = _uiState.value.copy(state = AssistantState.IDLE)
            }
            override fun onError(utteranceId: String?) {
                _uiState.value = _uiState.value.copy(state = AssistantState.ERROR, error = "TTS failed to speak.")
            }
        })
    }

    // When the ViewModel is cleared, stop and shut down TTS and STT resources.
    override fun onCleared() {
        super.onCleared()
        speechRecognizer.destroy()
        tts.stop()
        tts.shutdown()
    }
}