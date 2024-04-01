package com.alex.adapter.controller

import com.alex.adapter.rest.controller.BreedController
import com.alex.application.service.BreedImageFacade
import com.alex.application.service.BreedService
import com.alex.application.service.dto.ImageDto
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.ContentDisposition
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import java.nio.charset.Charset

@ExtendWith(MockitoExtension::class)
@WebFluxTest(BreedController::class)
class BreedControllerIT {

    @Autowired
    private lateinit var webTestClient: WebTestClient

    @MockBean
    private lateinit var breedService: BreedService

    @MockBean
    private lateinit var breedImageFacade: BreedImageFacade

    @Test
    fun `test getBreedImage`(): Unit = runBlocking {
        val breedName = "golden"
        val breedImageContent = "breedImageContent".toByteArray(Charset.defaultCharset())
        val breedImageResource = ImageDto(breedImageContent, breedName)
        `when`(breedImageFacade.getBreedImage(breedName)).thenReturn(breedImageResource)

        webTestClient.get().uri("/breeds/$breedName")
            .exchange()
            .expectStatus().isOk
            .expectHeader().contentType(MediaType.IMAGE_JPEG)
            .expectHeader().contentLength(breedImageResource.content.size.toLong())
            .expectHeader().contentDisposition(
                ContentDisposition.builder("attachment")
                    .filename("${breedImageResource.name}.jpg").build()
            )
            .expectBody()
            .consumeWith { response ->
                val expectedContent = breedImageResource.content
                val actualContent = response.responseBody!!
                assert(expectedContent.contentEquals(actualContent))
            }
    }

    @Test
    fun `test getBreedNames`(): Unit = runBlocking {
        val breedNames = listOf("Labrador", "Golden Retriever")

        `when`(breedService.findAll()).thenReturn(breedNames)

        val typeRef: ParameterizedTypeReference<List<String>> = object : ParameterizedTypeReference<List<String>>() {}

        webTestClient.get().uri("/breeds/list")
            .exchange()
            .expectStatus().isOk
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody(typeRef)
            .isEqualTo(breedNames)
    }
}
