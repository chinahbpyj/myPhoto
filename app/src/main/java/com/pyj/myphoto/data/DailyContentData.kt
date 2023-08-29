package com.pyj.myphoto.data

data class DailyContentData(
    val dataList: List<ContentItem>?
)

data class ContentItem(
    val src: String? = "",
    val text: String? = "",
    val year: String? = "",
    val day: String? = "",
    val month: String? = "",
    val content: String? = "",
    val note: String? = "",
    val dateline: String? = ""
)
