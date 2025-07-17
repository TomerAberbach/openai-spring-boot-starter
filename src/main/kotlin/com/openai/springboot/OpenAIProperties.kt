package com.openai.springboot

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.validation.annotation.Validated
import jakarta.validation.constraints.NotBlank

@ConfigurationProperties(prefix = "openai")
@Validated
internal data class OpenAIProperties(
    val baseUrl: String? = null,
    @field:NotBlank(message = "OpenAI API key is required. Please set openai.api-key property.")
    val apiKey: String,
    val organization: String? = null,
    val project: String? = null,
    val webhookSecret: String? = null,
)