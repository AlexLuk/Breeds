package com.alex.adapter.repository

import com.alex.domain.model.BreedImage
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface ImageRepository : CoroutineCrudRepository<BreedImage, Long> {
    suspend fun findImageByName(name: String): BreedImage?
}
