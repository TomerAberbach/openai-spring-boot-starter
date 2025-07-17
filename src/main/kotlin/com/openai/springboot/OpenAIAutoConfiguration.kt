package com.openai.springboot

import com.openai.client.OpenAIClient
import com.openai.client.okhttp.OpenAIOkHttpClient
import org.springframework.beans.factory.ObjectProvider
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean

@AutoConfiguration
@ConditionalOnClass(OpenAIClient::class)
@EnableConfigurationProperties(OpenAIProperties::class)
class OpenAIAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    internal fun client(
        properties: OpenAIProperties,
        customizers: ObjectProvider<OpenAICustomizer>
    ): OpenAIClient {
        if (properties.apiKey.isBlank()) {
            throw IllegalArgumentException("OpenAI API key cannot be blank. Please set openai.api-key property.")
        }
        
        return OpenAIOkHttpClient.builder().apply {
            properties.baseUrl?.let(::baseUrl)
            apiKey(properties.apiKey)
            properties.organization?.let(::organization)
            properties.project?.let(::project)
            properties.webhookSecret?.let(::webhookSecret)
            customizers.orderedStream().forEach { it.customize(this) }
        }.build()
    }
}