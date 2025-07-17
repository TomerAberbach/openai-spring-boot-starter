package com.openai.springboot

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "openai")
internal data class OpenAIProperties(
    val baseUrl: String? = null,
    val apiKey: String? = null,
    val organization: String? = null,
    val project: String? = null,
    val webhookSecret: String? = null,
)