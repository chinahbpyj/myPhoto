package com.pyj.myphoto.data.px

import com.pyj.myphoto.api.PxApi
import com.pyj.myphoto.data.PxPhoto

class PxImpl : PxRepository {
    override suspend fun curated(
        ty: String?,
        page: String?,
        size: String?,
        type: String?
    ): List<PxPhoto>? {
        return try {
            PxApi.createPxApi().curated(
                ty,
                page,
                size,
                type
            )
        } catch (ex: Exception) {
            null
        }
    }
}