package com.pyj.myphoto.data.px

import com.pyj.myphoto.data.PxPhoto

interface PxRepository {
    suspend fun curated(
        ty: String?,
        page: String?,
        size: String?,
        type: String?
    ): List<PxPhoto>?
}