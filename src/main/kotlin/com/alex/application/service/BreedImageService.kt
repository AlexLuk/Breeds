package com.alex.application.service

import com.alex.adapter.repository.ImageRepository
import com.alex.domain.model.BreedImage
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

private val log: org.slf4j.Logger = LoggerFactory.getLogger(BreedImageService::class.java)


@Service
class BreedImageService(
    private val imageRepository: ImageRepository
) {
    suspend fun getImage(breedName: String): BreedImage? {
        return imageRepository.findImageByName(breedName)
    }

    suspend fun saveImage(breedImage: BreedImage): BreedImage {
        val saveDeferred = CompletableDeferred<BreedImage>()
        runBlocking {
            launch {
                log.info("Saving image for breed ${breedImage.name}")
                val save = imageRepository.save(breedImage)
                saveDeferred.complete(save)
            }
        }
        return runBlocking { saveDeferred.await() }
    }
}