package com.example.krishimitra.presentation.home_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.krishimitra.Constants
import com.example.krishimitra.domain.repo.Repo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    val repo: Repo
): ViewModel() {

    private val _state = MutableStateFlow<HomeScreenState>(HomeScreenState())
    val state = _state.asStateFlow()



    init {
        getLanguage()
    }

    fun onEvent(event: HomeScreenEvent){
        when(event){
            is HomeScreenEvent.ChangeLanguage -> {
                changeLanguage(event.lang)
            }
        }
    }

    private fun getLanguage() {
        viewModelScope.launch(Dispatchers.IO) {
            repo.getLanguage().collectLatest { langcode ->
                _state.update {
                    it.copy(
                        currentLanguage = Constants.SUPPORTED_LANGUAGES
                            .find { it.code == langcode }
                            ?.nativeName ?: "English"
                    )
                }
            }
        }
    }

    fun changeLanguage(langCode: String) {
        viewModelScope.launch {
            repo.changeLanguage(langCode)
            getLanguage()
        }
    }
}