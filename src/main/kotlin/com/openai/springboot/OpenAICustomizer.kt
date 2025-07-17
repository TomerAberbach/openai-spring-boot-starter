package com.openai.springboot

import com.openai.client.okhttp.OpenAIOkHttpClient

fun interface OpenAICustomizer {

    fun customize(builder: OpenAIOkHttpClient.Builder)
}
