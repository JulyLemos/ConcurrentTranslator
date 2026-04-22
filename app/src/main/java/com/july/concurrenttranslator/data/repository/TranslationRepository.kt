package com.july.concurrenttranslator.data.repository

import com.july.concurrenttranslator.data.network.TranslationApi

class TranslationRepository {

    suspend fun translate(
        sourceLang: String,
        targetLang: String,
        inputText: String
    ): Result<String> {
        return try {
            val result = TranslationApi.translate(sourceLang, targetLang, inputText)
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}