package com.alex.adapter.controller

import com.alex.adapter.rest.controller.BreedController
import com.alex.application.service.BreedImageFacade
import com.alex.application.service.BreedService
import com.alex.application.service.dto.ImageDto
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.springframework.core.io.ByteArrayResource
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import java.nio.charset.Charset
import org.mockito.Mockito.`when` as mockitoWhen

class BreedControllerTest {

    private val breedService: BreedService = mock(BreedService::class.java)

    private val breedImageFacade: BreedImageFacade = mock(BreedImageFacade::class.java)

    private val breedController = BreedController(breedService, breedImageFacade)


    @Test
    fun shouldReturnBreedImageWithCorrectHeadersWhenNameIsValid(): Unit = runBlocking {
        val name = "labrador"
        val breedImageContent = "breedImageContent".toByteArray(Charset.defaultCharset())
        val breedImageName = "labrador"
        mockitoWhen(breedImageFacade.getBreedImage(name)).thenReturn(
            ImageDto(
                content = breedImageContent,
                name = breedImageName
            )
        )

        val responseEntity = breedController.getBreedImage(name)

        assertEquals(HttpStatus.OK, responseEntity.statusCode)
        assertEquals(MediaType.IMAGE_JPEG, responseEntity.headers.contentType)
        assertEquals(
            "attachment; filename=labrador.jpg",
            responseEntity.headers.getFirst(HttpHeaders.CONTENT_DISPOSITION)
        )
        assertEquals(breedImageContent.size.toLong(), responseEntity.headers.contentLength)
        assertEquals(ByteArrayResource(breedImageContent), responseEntity.body)
    }

    @Test
    fun shouldReturnListOfBreedNames(): Unit = runBlocking {
        val breedNames = listOf("labrador", "poodle", "bulldog")
        mockitoWhen(breedService.findAll()).thenReturn(breedNames)

        val responseEntity = breedController.getBreedNames()

        assertEquals(responseEntity, breedNames)
    }
}
