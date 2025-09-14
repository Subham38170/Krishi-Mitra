package com.example.krishimitra.presentation.mandi_screen

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.krishimitra.data.local.entity.MandiPriceEntity
import com.example.krishimitra.data.local.json.loadStatesAndDistricts
import com.example.krishimitra.domain.repo.Repo
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MandiScreenViewModel @Inject constructor(
    private val repo: Repo,
    @ApplicationContext private val context: Context
) : ViewModel() {
    private val _state = MutableStateFlow(MandiScreenState())
    val state = _state.asStateFlow()
    var pagingData: MutableStateFlow<PagingData<MandiPriceEntity>> =
        MutableStateFlow<PagingData<MandiPriceEntity>>(PagingData.empty())

    init {
        getMandiPrices()
        loadStates()
    }


    fun onEvent(event: MandiPriceScreenEvent) {
        when (event) {
            MandiPriceScreenEvent.onDistrictDeselect -> {
                _state.update { it.copy(district = "") }
                getMandiPrices(state = state.value.state)
            }

            is MandiPriceScreenEvent.onDistrictSelect -> {
                _state.update { it.copy(district = event.district) }
                getMandiPrices(state = state.value.state, district = state.value.district)
            }

            MandiPriceScreenEvent.onStateDeselect -> {
                _state.update { it.copy(state = "") }
                getMandiPrices()
            }

            is MandiPriceScreenEvent.onStateSelect -> {
                _state.update { it.copy(state = event.state) }
                loadDistricts()
                getMandiPrices(state = event.state)

            }

            is MandiPriceScreenEvent.onSearch -> {
                getMandiPrices(
                    state = if (state.value.state.isNotEmpty()) state.value.state else null,
                    district = if (state.value.district.isNotEmpty()) state.value.district else null,
                    commodity = event.searchText
                )
            }
        }
    }

    private fun loadStates() {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update {
                it.copy(
                    listOfStates = loadStatesAndDistricts(context).map { it.state }
                )
            }
        }
    }

    private fun loadDistricts() {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update {
                it.copy(
                    listOfDistricts = loadStatesAndDistricts(context).filter { it.state == state.value.state }
                        .map { it.districts }.get(0)
                )
            }
        }
    }

    fun getMandiPrices(
        state: String? = null,
        district: String? = null,
        market: String? = null,
        commodity: String? = null,
        variety: String? = null,
        grade: String? = null

    ) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.getMandiPricesPaging(
                state = state,
                district = district,
                market = market,
                commodity = commodity,
                variety = variety,
                grade = grade
            ).cachedIn(viewModelScope)
                .collectLatest { data ->
                    pagingData.value = data
                }
        }

    }


}