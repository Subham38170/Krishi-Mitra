package com.example.krishimitra.presentation.home_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.krishimitra.Constants
import com.example.krishimitra.domain.ResultState
import com.example.krishimitra.domain.repo.Repo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    val repo: Repo
) : ViewModel() {

    private val _state = MutableStateFlow<HomeScreenState>(HomeScreenState())
    val state = _state.asStateFlow()


    private val _eventFlow = MutableSharedFlow<HomeScreenEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        getLanguage()
        loadGovtSchemes()
        loadKrishiNews()
    }

    fun onEvent(event: HomeScreenEvent) {
        when (event) {
            is HomeScreenEvent.ChangeLanguage -> {
                changeLanguage(event.lang)
            }

            is HomeScreenEvent.Error ->{

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

    private fun loadGovtSchemes() {
        viewModelScope.launch {

            repo.loadGovtSchemes()
                .collectLatest { action ->
                    when (action) {
                        is ResultState.Error -> {
                            _eventFlow.emit(HomeScreenEvent.Error(action.exception))
                            _state.update {
                                it.copy(
                                    isBannersLoading = false
                                )
                            }
                        }

                        ResultState.Loading -> {
                            _state.update {
                                it.copy(
                                    isBannersLoading = true
                                )
                            }
                        }

                        is ResultState.Success -> {
                            _state.update {
                                it.copy(
                                    isBannersLoading = true,
                                    schemeBannersList = action.data
                                )
                            }
                        }
                    }

                }
        }
    }

    private fun loadKrishiNews() {
        viewModelScope.launch {

            repo.loadKrishiNews()
                .collectLatest { action ->
                    when (action) {
                        is ResultState.Error -> {
                            _eventFlow.emit(HomeScreenEvent.Error(action.exception))
                            _state.update {
                                it.copy(
                                    isKrishiNewsLoading = false
                                )
                            }
                        }

                        ResultState.Loading -> {
                            _state.update {
                                it.copy(
                                    isKrishiNewsLoading = true
                                )
                            }
                        }

                        is ResultState.Success -> {
                            _state.update {
                                it.copy(
                                    isKrishiNewsLoading = true,
                                    krishiNewsBannerList = action.data
                                )
                            }
                        }
                    }

                }
        }
    }

}