package com.alex.application.service

import com.alex.adapter.repository.ImageRepository
import com.alex.application.service.BreedImageService
import com.alex.domain.model.BreedImage
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when` as mockitoWhen


class BreedImageServiceTest {
    private val imageRepository: ImageRepository = mock(ImageRepository::class.java)
    private val breedImageService: BreedImageService = BreedImageService(imageRepository)

    @Test
    fun shouldReturnImageFromRepository(): Unit = runBlocking {
        //given
        val breedName = "breedName"
        val breedImage = BreedImage(
            id = 1,
            name = breedName,
            content = ByteArray(1)
        )
        mockitoWhen(imageRepository.findImageByName(breedName)).thenReturn(breedImage)
        //when
        val result = breedImageService.getImage(breedName)
        //then
        assertThat(result).isEqualTo(breedImage)
    }

    @Test
    fun shouldSaveImageToRepository(): Unit = runBlocking {
        //given
        val breedName = "breedName"
        val breedImage = BreedImage(
            name = breedName,
            content = ByteArray(1)
        )
        mockitoWhen(imageRepository.save(breedImage)).thenAnswer { invocation ->
            invocation.getArgument<BreedImage>(0).copy(id = 1)
        }
        //when
        val result = breedImageService.saveImage(breedImage)
        //then
        assertThat(result).isEqualTo(breedImage.copy(id = 1))

    }
}