package com.example.krishimitra.presentation.mandi_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.krishimitra.data.local.entity.MandiPriceEntity
import com.example.krishimitra.domain.repo.Repo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject


@HiltViewModel
class MandiScreenViewModel @Inject constructor(
    private val repo: Repo
) : ViewModel() {
    private val _state = MutableStateFlow(MandiScreenState())
    val state = _state.asStateFlow()
    lateinit var pagingData: StateFlow<PagingData<MandiPriceEntity>>

    init {
        getMandiPrices()
    }


    fun getMandiPrices(
        state: String? = null,
        district: String? = null,
        market: String? = null,
        commodity: String? = null,
        variety: String? = null,
        grade: String? = null

    ) {
        val result = repo.getMandiPricesPaging(
            state = state,
            district = district,
            market = market,
            commodity = commodity,
            variety = variety,
            grade = grade
        ).cachedIn(viewModelScope)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = PagingData.empty()
            )

        pagingData = result

    }


}