package com.pyj.myphoto.viewmodel.you

import com.pyj.myphoto.data.YouPhoto

val titles = listOf("最新", "热门", "推荐")
val numbers = listOf("latest", "hot", "")

data class YouState(
    val request: Boolean = true,
    val index: Int = 1,
    val loading: Boolean = false,
    val refreshing: Boolean = false,
    val keyName: String = titles[2],
    val valueName: String = numbers[2],
    val keyType: List<String> = titles,
    val valueType: List<String> = numbers,
    val photos: List<YouPhoto>? = null
) {
    fun toUiState(): YouUiState = YouUiState(
        request = request,
        index = index,
        loading = loading,
        refreshing = refreshing,
        keyName = keyName,
        valueName = valueName,
        keyType = keyType,
        valueType = valueType,
        photos = photos
    )
}

data class YouUiState(
    val request: Boolean = true,
    val index: Int = 1,
    val loading: Boolean = false,
    val refreshing: Boolean = false,
    val keyName: String = titles[2],
    val valueName: String = numbers[2],
    val keyType: List<String> = titles,
    val valueType: List<String> = numbers,
    val photos: List<YouPhoto>? = null
)