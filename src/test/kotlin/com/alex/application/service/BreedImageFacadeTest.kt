package com.alex.application.service

import com.alex.adapter.client.BreedClient
import com.alex.application.service.dto.ImageDto
import com.alex.domain.model.Breed
import com.alex.domain.model.BreedImage
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when` as mockitoWhen

class BreedImageFacadeTest {
    private val breedService: BreedService = mock(BreedService::class.java)

    private val breeBreedImageService: BreedImageService = mock(BreedImageService::class.java)

    private val breedClient: BreedClient = mock(BreedClient::class.java)

    private val breedImageFacade: BreedImageFacade =
        BreedImageFacade(breedService, breeBreedImageService, breedClient)

    @Test
    fun shouldReturnBreedImageFromDBWhenFound(): Unit = runBlocking {
        //given
        val breedName = "breedName"
        val fileName = "fileName"
        val breedId: Long = 1
        val byteArray = ByteArray(1)
        val expectedImageDto = ImageDto(byteArray, fileName)
        val imageId: Long = 2
        mockitoWhen(breedService.findByName(breedName)).thenReturn(Breed(breedId, breedName))
        mockitoWhen(breeBreedImageService.getImage(breedName)).thenReturn(BreedImage(imageId, fileName, byteArray))
        //when
        val result = breedImageFacade.getBreedImage(breedName)
        //then
        assertThat(result).isEqualTo(expectedImageDto)
    }


    @Test
    fun shouldGetBreedImageFromClientWhenNotFoundInDB(): Unit = runBlocking {
        //given
        val breedName = "breedName"
        val fileName = "fileName"
        val breedId: Long = 1
        val byteArray = ByteArray(1)
        val expectedImageDto = ImageDto(byteArray, fileName)
        val imageId: Long = 2
        //when
        mockitoWhen(breedService.findByName(breedName)).thenReturn(Breed(breedId, breedName))
        mockitoWhen(breeBreedImageService.getImage(breedName)).thenReturn(null)
        val foundImageDto = ImageDto(byteArray, fileName)
        mockitoWhen(breedClient.getImage(breedName)).thenReturn(foundImageDto)
        val breedImage = BreedImage(
            imageId,
            fileName,
            byteArray
        )
        mockitoWhen(breeBreedImageService.saveImage(breedImage.copy(id = null))).thenReturn(
            breedImage
        )
        val result = breedImageFacade.getBreedImage(breedName)
        //then
        assertThat(result).isEqualTo(expectedImageDto)
    }
}
