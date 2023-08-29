package com.pyj.myphoto.viewmodel.app

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.pyj.myphoto.api.DailyApi
import com.pyj.myphoto.data.ContentItem
import com.pyj.myphoto.data.ShareData
import com.pyj.myphoto.data.app.AppRepository
import com.pyj.myphoto.data.app.UserData
import com.pyj.myphoto.store.setBooleanValue
import com.pyj.myphoto.util.CACHE_KEY_DARK_THEME
import com.pyj.myphoto.util.CACHE_KEY_ROW
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@SuppressLint("StaticFieldLeak")
class AppViewModel(val context: Context) : ViewModel() {
    private val viewModelState = MutableStateFlow(AppState())

    val uiState = viewModelState
        .map(AppState::toUiState)
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            viewModelState.value.toUiState()
        )

    fun daily() {
        viewModelScope.launch {
            try {
                val data = DailyApi.createPxApi().daily(DailyApi.BASE_URL)

                viewModelState.update {
                    it.copy(
                        dailyData = data,
                        request = false,
                        imgUrl = data.picture2,
                        shareData = ShareData(data.content, data.note, data.dateline),
                    )
                }
            } catch (ex: Exception) {
                viewModelState.update {
                    it.copy(
                        request = false
                    )
                }
            }
        }
    }

    fun imgUrl(imgUrl: String) {
        viewModelState.update {
            it.copy(
                imgUrl = imgUrl
            )
        }
    }

    fun contentData(contentData: ContentItem) {
        val shareData = ShareData(
            contentData.content,
            contentData.note,
            contentData.dateline
        )
        viewModelState.update {
            it.copy(
                shareData = shareData,
                imgUrl = contentData.src ?: "",
            )
        }
    }

    fun initUserData(appRepository: AppRepository, default: Boolean) {
        viewModelScope.launch {
            val userData = appRepository.userData(default)

            viewModelState.update {
                it.copy(
                    userData = userData
                )
            }
        }
    }

    fun setUserDarkTheme(value: Boolean) {
        viewModelScope.launch {
            setBooleanValue(context, keyName = CACHE_KEY_DARK_THEME, value = value)

            val userData = viewModelState.value.userData

            viewModelState.update {
                it.copy(
                    userData = UserData(
                        darkTheme = value,
                        row = userData?.row ?: false
                    )
                )
            }
        }
    }

    fun setUserShowRow(value: Boolean) {
        viewModelScope.launch {
            setBooleanValue(context, keyName = CACHE_KEY_ROW, value = value)

            val userData = viewModelState.value.userData

            viewModelState.update {
                it.copy(
                    userData = UserData(
                        darkTheme = userData?.darkTheme ?: false,
                        row = value
                    )
                )
            }
        }
    }

    fun networkConnectChanged(value: Boolean) {
        viewModelState.update {
            it.copy(
                networkConnected = value
            )
        }
    }

    companion object {
        fun provideFactory(context: Context): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return AppViewModel(context) as T
                }
            }
    }
}
