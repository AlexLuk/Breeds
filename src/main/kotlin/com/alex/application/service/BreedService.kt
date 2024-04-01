package com.alex.application.service

import com.alex.adapter.repository.BreedRepository
import com.alex.application.service.exception.BreedNotFoundException
import com.alex.domain.model.Breed
import org.springframework.stereotype.Service

@Service
class BreedService(
    private val breedRepository: BreedRepository
) {
    suspend fun findByName(breedName: String): Breed {
        return breedRepository.findBreedByName(breedName) ?: throw BreedNotFoundException(breedName)
    }

    suspend fun findAll(): List<String> {
        return breedRepository.findAllBreedNames()
    }

}

