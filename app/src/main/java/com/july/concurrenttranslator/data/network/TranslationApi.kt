package com.july.concurrenttranslator.data.network

import com.july.concurrenttranslator.data.model.TranslationResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST

interface TranslationApi {

    @Headers(
        "x-rapidapi-host: free-google-translator.p.rapidapi.com",
        "x-rapidapi-key: 0efca81af1mshee5312859e2d216p1d7e9ajsna283087c44d0"
    )
    @FormUrlEncoded
    @POST("v1/translateList")
    suspend fun translate(
        @Field("query") text: String,
        @Field("source") sourceLang: String,
        @Field("target") targetLang: String
    ): TranslationResponse
}