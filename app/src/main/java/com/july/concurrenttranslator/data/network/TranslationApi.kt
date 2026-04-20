package com.july.concurrenttranslator.data.network

import com.july.concurrenttranslator.data.model.TranslationResponse
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface TranslationApi {
    @Headers(
        "x-rapidapi-host: text-translation2.p.rapidapi.com",
        "x-rapidapi-key: 0efca81af1mshee5312859e2d216p1d7e9ajsna283087c44d0"
    )
    @GET("translate")
    suspend fun translate(
        @Query("source_lang") sourceLang: String,
        @Query("target_lang") targetLang: String,
        @Query("input_text") inputText: String
    ): TranslationResponse
}