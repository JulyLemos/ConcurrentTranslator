package com.july.concurrenttranslator.data.network

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

object TranslationApi {

    private val client = OkHttpClient()

    fun translate(sourceLang: String, targetLang: String, text: String): String {
        val mediaType = "application/json".toMediaType()
        val jsonBody = "{\"translate\":\"${text.replace("\"", "\\\"")}\"}"
        val body = jsonBody.toRequestBody(mediaType)
        val encodedText = java.net.URLEncoder.encode(text, "UTF-8")

        val request = Request.Builder()
            .url("https://free-google-translator.p.rapidapi.com/external-api/free-google-translator?from=$sourceLang&to=$targetLang&query=$encodedText")
            .post(body)
            .addHeader("x-rapidapi-key", "0efca81af1mshee5312859e2d216p1d7e9ajsna283087c44d0")
            .addHeader("x-rapidapi-host", "free-google-translator.p.rapidapi.com")
            .addHeader("Content-Type", "application/json")
            .build()

        val response = client.newCall(request).execute()
        val responseBody = response.body!!.string()

        if (!response.isSuccessful) throw Exception("HTTP ${response.code}: $responseBody")

        val json = JSONObject(responseBody)
        return json.getString("translation")
    }
}