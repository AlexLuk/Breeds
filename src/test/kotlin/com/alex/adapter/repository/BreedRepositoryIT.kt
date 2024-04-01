package com.alex.adapter.repository

import com.alex.adapter.repository.BreedRepository
import com.alex.domain.model.Breed
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class BreedRepositoryIT(
    @Autowired val breedRepository: BreedRepository
) : AbstractRepositoryIT() {

    companion object {

        @BeforeAll
        @JvmStatic
        fun insertBreeds(@Autowired breedRepository: BreedRepository) {
            val breeds = listOf(
                Breed(name = "Labrador Retriever"),
                Breed(name = "German Shepherd Dog"),
            )
            runBlocking {
                breeds.forEach { breedRepository.save(it) }
            }
        }
    }

    @Test
    fun shouldFindBreedByName() = runBlocking {
        // given
        val breed = Breed(name = "Labrador Retriever")

        // when
        val foundBreed = breedRepository.findBreedByName("Labrador Retriever")

        // then
        assertEquals(breed.copy(id = foundBreed!!.id), foundBreed)
    }

    @Test
    fun shouldFindAllBreedNames() = runBlocking {
        // given
        // when
        val breedNames = breedRepository.findAllBreedNames()

        // then
        assertEquals(listOf("Labrador Retriever", "German Shepherd Dog"), breedNames)
    }

    @Test
    fun shouldFindFirstBreed() = runBlocking {
        // given
        val breed1 = Breed(name = "Labrador Retriever")

        // when
        val foundBreed = breedRepository.findFirst()

        // then
        assertEquals(breed1.copy(id = foundBreed!!.id), foundBreed)
    }
}