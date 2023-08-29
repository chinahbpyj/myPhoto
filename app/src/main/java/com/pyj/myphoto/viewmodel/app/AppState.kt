package com.pyj.myphoto.viewmodel.app

import com.pyj.myphoto.data.DailyData
import com.pyj.myphoto.data.ShareData
import com.pyj.myphoto.data.app.UserData

data class AppState(
    val shareData: ShareData? = null,
    val userData: UserData? = null,
    val dailyData: DailyData? = null,
    val imgUrl: String = "",
    val request: Boolean = true,
    val networkConnected: Boolean = true,
) {
    fun toUiState(): AppUiState = AppUiState(
        shareData = shareData,
        userData = userData,
        dailyData = dailyData,
        imgUrl = imgUrl,
        request = request,
        networkConnected = networkConnected
    )
}

data class AppUiState(
    val shareData: ShareData? = null,
    val userData: UserData? = null,
    val dailyData: DailyData? = null,
    val imgUrl: String = "",
    val request: Boolean = true,
    val networkConnected: Boolean = true,
)

