package com.alex.application.service


import com.alex.adapter.client.BreedClient
import com.alex.adapter.repository.BreedRepository
import com.alex.domain.model.Breed
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.boot.DefaultApplicationArguments
import org.mockito.Mockito.`when` as mockitoWhen

class AppStartupRunnerTest {
    private val breedRepository: BreedRepository = mock(BreedRepository::class.java)
    private val breedClient: BreedClient = mock(BreedClient::class.java)
    private val appStartupRunner = AppStartupRunner(breedRepository, breedClient)

    @Test
    fun shouldInitializeDatabaseWhenItIsEmpty(): Unit = runBlocking {
        // given
        mockitoWhen(breedRepository.findFirst()).thenReturn(null)
        mockitoWhen(breedClient.getAllBreeds()).thenReturn(setOf("breed1", "breed2"))
        val savedBreedsFlow = flowOf(Breed(name = "breed1"), Breed(name = "breed2"))
        mockitoWhen(breedRepository.saveAll(anyList())).thenReturn(savedBreedsFlow)
        // when
        appStartupRunner.run(DefaultApplicationArguments())

        // then
        verify(breedRepository, times(1)).saveAll(listOf(Breed(name = "breed1"), Breed(name = "breed2")))
    }

    @Test
    fun shouldNotInitializeDatabaseWhenItIsNotEmpty(): Unit = runBlocking {
        // given
        mockitoWhen(breedRepository.findFirst()).thenReturn(Breed(name = "breed"))

        // when
        appStartupRunner.run(DefaultApplicationArguments())

        // then
        verifyNoInteractions(breedClient)
        verify(breedRepository, never()).saveAll(anyList())
    }
}