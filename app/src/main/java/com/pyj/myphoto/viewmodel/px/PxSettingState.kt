package com.pyj.myphoto.viewmodel.px

const val TYPE_1 = 1

val titlesType1 = listOf("新作", "排名", "热门", "推荐")
val numbersType1 = listOf("created_date", "rankingRise", "rating", "recommendTime")

data class PxSettingState(
    val type: Int = TYPE_1,
    val keyName: String = titlesType1[2],
    val valueName: String = numbersType1[2],
    val keyType1: List<String> = titlesType1,
    val valueType1: List<String> = numbersType1,
) {
    fun toUiState(): PxSettingUiState = PxSettingUiState(
        type = type,
        keyName = keyName,
        valueName = valueName,
        keyType1 = keyType1,
        valueType1 = valueType1,
    )
}

data class PxSettingUiState(
    val type: Int = TYPE_1,
    val keyName: String = titlesType1[2],
    val valueName: String = numbersType1[2],
    val keyType1: List<String> = titlesType1,
    val valueType1: List<String> = numbersType1,
)

