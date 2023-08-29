package com.pyj.myphoto.viewmodel.daily

import com.pyj.myphoto.data.ContentItem

data class DailyState(
    val request: Boolean = true,
    val contents: List<ContentItem>? = null
) {
    fun toUiState(): DailyUiState = DailyUiState(
        request = request,
        contents = contents
    )
}

data class DailyUiState(
    val request: Boolean = true,
    val contents: List<ContentItem>? = null
)

