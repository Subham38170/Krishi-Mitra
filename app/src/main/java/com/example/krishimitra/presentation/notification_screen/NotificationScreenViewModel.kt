package com.example.krishimitra.presentation.notification_screen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.krishimitra.data.mappers.toNotificationModel
import com.example.krishimitra.domain.notification_model.GlobalNotificationData
import com.example.krishimitra.domain.repo.Repo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class NotificationScreenViewModel @Inject constructor(
    private val saveStateHandle: SavedStateHandle,
    private val repo: Repo
) : ViewModel() {
    private val _state = MutableStateFlow(NotificationScreenState())
    val state = _state.asStateFlow()

    init {
        loadAllNotifications()
    }

    private fun loadAllNotifications() {
        viewModelScope.launch(Dispatchers.IO) {
            repo.getAllNotifications().collect { notifications ->
                _state.update {
                    it.copy(
                        notificationList = notifications.map { it.toNotificationModel() }
                    )
                }
            }
        }
    }

    private fun handleNotificationData() {

        val title = saveStateHandle.get<String>("title")
        val body = saveStateHandle.get<String>("body")
        val imageUrl = saveStateHandle.get<String>("imageUrl")
        val webLink = saveStateHandle.get<String>("webLink")
        _state.update {
            it.copy(
                notificationList = listOf(
                    GlobalNotificationData(
                        title = title ?: "KrishiMitra",
                        description = body ?: "Error",
                        imageUrl = imageUrl,
                        webLink = webLink
                    )
                )
            )
        }


    }


}