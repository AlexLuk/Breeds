package com.alex.config

import com.alex.config.properties.BreedClientProperties
import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
@EnableConfigurationProperties(BreedClientProperties::class)
@OpenAPIDefinition(info = Info(title = "Swagger Demo", version = "1.0", description = "Documentation APIs v1.0"))
class ApplicationConfiguration {
    @Bean
    fun webClient(): WebClient {
        return WebClient.builder()
            .build()
    }
}
