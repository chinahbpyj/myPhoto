package com.pyj.myphoto.data

data class PxPhoto(
    val width: Int,
    val height: Int,
    val title: String,
    val url: PxUrl
)

data class PxUrl(
    val baseUrl: String,
    val id: String,
    val p1: String,
    val p2: String,
    val p3: String,
    val p4: String
)