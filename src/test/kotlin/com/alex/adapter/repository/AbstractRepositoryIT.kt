package com.alex.adapter.repository

import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Import
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource

@DataR2dbcTest
@Import(AbstractRepositoryIT.DBTestConfig::class)
abstract class AbstractRepositoryIT {
    @TestConfiguration
    @EnableAutoConfiguration
    @EnableR2dbcRepositories(
        basePackages = ["com.alex.adapter.repository"]
    )
    class DBTestConfig

    companion object {
        @DynamicPropertySource
        @JvmStatic
        fun configureH2Database(registry: DynamicPropertyRegistry) {
            registry.add("spring.r2dbc.url") { "r2dbc:h2:mem:///test?options=DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE" }
            registry.add("spring.r2dbc.username") { "sa" }
            registry.add("spring.r2dbc.password") { "" }
        }
    }
}
