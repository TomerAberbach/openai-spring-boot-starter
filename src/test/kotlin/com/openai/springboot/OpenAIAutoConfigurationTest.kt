package com.openai.springboot

import com.openai.client.OpenAIClient
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.getBean
import org.springframework.boot.autoconfigure.AutoConfigurations
import org.springframework.boot.test.context.runner.ApplicationContextRunner
import java.util.function.Supplier
import kotlin.test.assertEquals

internal class OpenAIAutoConfigurationTest {

    private val contextRunner = ApplicationContextRunner()
        .withConfiguration(AutoConfigurations.of(OpenAIAutoConfiguration::class.java))

    @Test
    fun properties() {
        contextRunner
            .withPropertyValues(
                "openai.api-key=test-key",
                "openai.organization=test-org",
                "openai.project=test-project",
                "openai.webhook-secret=test-secret",
                "openai.base-url=https://example.com"
            )
            .run { context ->
                val properties = context.getBean<OpenAIProperties>()
                assertEquals("test-key", properties.apiKey)
                assertEquals("test-org", properties.organization)
                assertEquals("test-project", properties.project)
                assertEquals("test-secret", properties.webhookSecret)
                assertEquals("https://example.com", properties.baseUrl)
            }
    }

    @Test
    fun clientWithRequiredFields() {
        assertDoesNotThrow {
            contextRunner
                .withPropertyValues(
                    "openai.api-key=test-key",
                    "openai.organization=test-org",
                    "openai.project=test-project",
                    "openai.webhook-secret=test-secret",
                    "openai.base-url=https://custom.api.com/v1"
                )
                .run { it.getBean<OpenAIClient>() }
        }
    }

    @Test
    fun clientWithCustomizers() {
        var customized = false
        contextRunner
            .withPropertyValues("openai.api-key=test-key")
            .withBean(OpenAICustomizer::class.java, Supplier { OpenAICustomizer { customized = true } })
            .run {
                it.getBean<OpenAIClient>()
                assertTrue(customized)
            }
    }

    @Test
    fun clientWithBlankApiKey() {
        val exception = assertThrows<Exception> {
            contextRunner
                .withPropertyValues(
                    "openai.api-key=   ",
                    "openai.organization=test-org"
                )
                .run { it.getBean<OpenAIClient>() }
        }

        assertTrue(
            exception.stackTraceToString().contains("OpenAI API key is required. Please set openai.api-key property.")
        )
    }
}
