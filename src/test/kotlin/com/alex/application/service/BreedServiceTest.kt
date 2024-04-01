package com.alex.application.service

import com.alex.adapter.repository.BreedRepository
import com.alex.application.service.BreedService
import com.alex.application.service.exception.BreedNotFoundException
import com.alex.domain.model.Breed
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when` as mockitoWhen

class BreedServiceTest {
    private val breedRepository: BreedRepository = mock(BreedRepository::class.java)

    private val breedService: BreedService = BreedService(breedRepository)

    @Test
    fun shouldFindBreedByName(): Unit = runBlocking {
        // given
        val breedName = "breedName"
        val breedId: Long = 1
        val breed = Breed(breedId, breedName)
        mockitoWhen(breedRepository.findBreedByName(breedName)).thenReturn(breed)

        // when
        val result = breedService.findByName(breedName)

        // then
        assertThat(result).isEqualTo(breed)
    }

    @Test
    fun shouldThrowExceptionWhenBreedNotFound(): Unit = runBlocking {
        // given
        val breedName = "breedName"
        mockitoWhen(breedRepository.findBreedByName(breedName)).thenReturn(null)

        // when, then
        assertThrows<BreedNotFoundException> { breedService.findByName(breedName) }.let {
            assertThat(it).extracting(breedName).isEqualTo(breedName)
        }
    }

    @Test
    fun shouldFindAllBreedNames(): Unit = runBlocking {
        // given
        val breedNames = listOf("breed1", "breed2")
        mockitoWhen(breedRepository.findAllBreedNames()).thenReturn(breedNames)

        // when
        val result = breedService.findAll()

        // then
        assertThat(result).containsExactlyInAnyOrderElementsOf(breedNames)
    }
}
