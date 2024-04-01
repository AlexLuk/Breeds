package com.alex.application.service

import com.alex.adapter.client.BreedClient
import com.alex.adapter.repository.BreedRepository
import com.alex.domain.model.Breed
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component

private val log: org.slf4j.Logger = LoggerFactory.getLogger(AppStartupRunner::class.java)

@Component
class AppStartupRunner(
    private val breedRepository: BreedRepository, private val breedClient: BreedClient
) : ApplicationRunner {
    override fun run(args: ApplicationArguments?) {
        runBlocking {
            launch {
                val databaseIsEmpty = breedRepository.findFirst() == null
                if (databaseIsEmpty) {
                    log.info("Database is empty")

                    val breeds = breedClient.getAllBreeds()

                    breedRepository.saveAll(breeds.map { breedName -> Breed(name = breedName) }).toList()
                        .let { log.info("Breeds were uploaded ${it.size}") }
                } else {
                    log.info("Database was initialized")
                }
            }
        }
    }
}