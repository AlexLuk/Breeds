package com.alex.adapter.rest.controller

import com.alex.application.service.BreedImageFacade
import com.alex.application.service.BreedService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.core.io.ByteArrayResource
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Breeds information")
@RequestMapping("/breeds")
@RestController
class BreedController(
    private val breedService: BreedService,
    private val breedImageFacade: BreedImageFacade
) {

    @Operation(summary = "Get breed image by breed name")
    @ApiResponses(
        value = [ApiResponse(
            description = "Breed image",
            responseCode = "200"
        ), ApiResponse(
            description = "Not found. Should be returned when either breed or image was not found",
            responseCode = "404",
        )]
    )
    @GetMapping("/{name}")
    suspend fun getBreedImage(@PathVariable name: String): ResponseEntity<Resource> {
        val breedImageResource = breedImageFacade.getBreedImage(name)
        val resource = ByteArrayResource(breedImageResource.content)
        val headers = HttpHeaders()
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=${breedImageResource.name}.jpg")
        headers.add(HttpHeaders.CONTENT_LENGTH, breedImageResource.content.size.toString())
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_JPEG.toString())
        return ResponseEntity.ok().headers(headers).body(resource)
    }

    @Operation(summary = "List all available breed names")
    @ApiResponses(
        value = [ApiResponse(
            description = "List of all available breed names",
            responseCode = "200"
        )]
    )
    @GetMapping("/list")
    suspend fun getBreedNames(): List<String> {
        return breedService.findAll()
    }
}