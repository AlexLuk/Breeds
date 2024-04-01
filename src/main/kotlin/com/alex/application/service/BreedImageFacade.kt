package com.alex.application.service

import com.alex.adapter.client.BreedClient
import com.alex.application.service.dto.ImageDto
import com.alex.domain.model.Breed
import com.alex.domain.model.BreedImage
import org.springframework.stereotype.Service

@Service
class BreedImageFacade(
    private val breedService: BreedService,
    private val breeBreedImageService: BreedImageService,
    private val breedClient: BreedClient
) {
    suspend fun getBreedImage(name: String): ImageDto =
        breedService.findByName(name).let(Breed::name).let { breedName -> getImage(breedName) }
            .let { image -> ImageDto(image.content, image.name) }

   private suspend fun getImage(name: String): BreedImage =
       breeBreedImageService.getImage(name) ?: breedClient.getImage(name)
           .let { imageDto ->
               val breedImage = BreedImage(name = imageDto.name, content = imageDto.content)
               breeBreedImageService.saveImage(breedImage)
           }
}