package com.pyj.myphoto.viewmodel.daily

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.pyj.myphoto.api.DailyContentApi
import com.pyj.myphoto.data.ContentItem
import com.pyj.myphoto.data.DailyContentData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DailyViewModel : ViewModel() {
    private val viewModelState = MutableStateFlow(DailyState())

    val uiState = viewModelState
        .map(DailyState::toUiState)
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            viewModelState.value.toUiState()
        )

    fun request(contentItem: ContentItem) {
        viewModelState.update { it.copy(request = true) }

        viewModelScope.launch {
            val dailyContentData: DailyContentData? = try {
                DailyContentApi.createPxApi().daily(DailyContentApi.BASE_URL)
            } catch (ex: Exception) {
                null
            }

            if (dailyContentData != null && !dailyContentData.dataList.isNullOrEmpty()) {
                val contents = dailyContentData.dataList
                val list = mutableListOf<ContentItem>()
                list.add(contentItem)
                for (item in contents) {
                    list.add(
                        ContentItem(
                            src = item.src,
                            content = item.text ?: "",
                            dateline = item.year + "-" + item.month + "-" + item.day
                        )
                    )
                }

                viewModelState.update {
                    it.copy(
                        request = false,
                        contents = list
                    )
                }
            } else {
                viewModelState.update {
                    it.copy(
                        request = false,
                        contents = null
                    )
                }
            }
        }
    }

    companion object {
        fun provideFactory(): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return DailyViewModel() as T
            }
        }
    }
}
