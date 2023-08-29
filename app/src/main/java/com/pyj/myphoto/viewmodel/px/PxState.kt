package com.pyj.myphoto.viewmodel.px

import com.pyj.myphoto.data.PxPhoto

data class PxState(
    val request: Boolean = true,
    val index: Int = 1,
    val loading: Boolean = false,
    val refreshing: Boolean = false,
    val photos: List<PxPhoto>? = null
) {
    fun toUiState(): PxUiState = PxUiState(
        request = request,
        index = index,
        loading = loading,
        refreshing = refreshing,
        photos = photos
    )
}

data class PxUiState(
    val request: Boolean = true,
    val index: Int = 1,
    val loading: Boolean = false,
    val refreshing: Boolean = false,
    val photos: List<PxPhoto>? = null
)

