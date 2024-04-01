package com.alex.adapter.repository

import com.alex.domain.model.BreedImage
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class ImageRepositoryIT(
    @Autowired val imageRepository: ImageRepository
) : AbstractRepositoryIT() {

    companion object {

        @BeforeAll
        @JvmStatic
        fun insertImages(@Autowired breedRepository: ImageRepository) {
            val breeds = listOf(
                BreedImage(name = "Labrador Retriever", content = ByteArray(1)),
                BreedImage(name = "German Shepherd Dog", content = ByteArray(1)),
            )
            runBlocking {
                breeds.forEach { breedRepository.save(it) }
            }
        }
    }

    @Test
    fun shouldFindImageByName() = runBlocking {
        // given
        val image = BreedImage(name = "Labrador Retriever", content = ByteArray(1))

        // when
        val foundImage = imageRepository.findImageByName("Labrador Retriever")

        // then
        Assertions.assertEquals(image.copy(id = foundImage!!.id), foundImage)
    }
}