package com.july.concurrenttranslator.data.repository

import com.july.concurrenttranslator.data.network.RetrofitClient

class TranslationRepository {
    private val api = RetrofitClient.translationApi

    suspend fun translate(
        sourceLang: String,
        targetLang: String,
        inputText: String
    ): Result<String> {
        return try {
            val response = api.translate(inputText, sourceLang, targetLang)
            Result.success(response.translation)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}