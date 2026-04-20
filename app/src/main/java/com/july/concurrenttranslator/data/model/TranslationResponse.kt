package com.july.concurrenttranslator.data.model

import com.google.gson.annotations.SerializedName

data class TranslationResponse(
    @SerializedName("translatedText")
    val translatedText: String
)
