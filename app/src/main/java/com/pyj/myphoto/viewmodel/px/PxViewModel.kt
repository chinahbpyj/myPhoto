package com.pyj.myphoto.viewmodel.px

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.pyj.myphoto.data.PxPhoto
import com.pyj.myphoto.data.px.PxRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PxViewModel(private val pxRepository: PxRepository) : ViewModel() {
    private var index = 1

    private val viewModelState = MutableStateFlow(PxState())
    private val settingState = MutableStateFlow(PxSettingState())

    private var photos: List<PxPhoto>? = listOf()

    val uiState = viewModelState
        .map(PxState::toUiState)
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            viewModelState.value.toUiState()
        )

    val settingUiState = settingState
        .map(PxSettingState::toUiState)
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            settingState.value.toUiState()
        )

    fun settingUpdateType(type: Int) {
        settingState.update {
            it.copy(
                type = type,
            )
        }
    }

    fun settingUpdateValue(key: String, value: String) {
        settingState.update {
            it.copy(
                keyName = key,
                valueName = value,
            )
        }
    }

    fun request(value: String) {
        viewModelState.update { it.copy(request = true) }
        loadData(value)
    }

    fun refresh(value: String) {
        viewModelState.update { it.copy(refreshing = true) }
        loadData(value)
    }

    private fun loadData(value: String) {
        index = 1

        viewModelScope.launch {
            photos = getListData(value, index.toString())

            viewModelState.update {
                it.copy(
                    request = false,
                    photos = photos,
                    refreshing = false
                )
            }
        }
    }

    private suspend fun getListData(value: String, page: String): List<PxPhoto>? {
        return pxRepository.curated(
            ty = value,
            page = page,
            size = "20",
            type = "json"
        )
    }

    fun loadMore(value: String) {
        viewModelState.update { it.copy(loading = true) }
        index++

        val list = mutableListOf<PxPhoto>()
        list.addAll(photos!!)
        viewModelScope.launch {
            val lists = getListData(value, index.toString())
            if (lists == null) {
                viewModelState.update {
                    it.copy(loading = false)
                }
            } else {
                list.addAll(lists)
                photos = list

                viewModelState.update {
                    it.copy(photos = photos, loading = false)
                }
            }

        }
    }

    companion object {
        fun provideFactory(
            pxRepository: PxRepository
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return PxViewModel(pxRepository) as T
            }
        }
    }
}
