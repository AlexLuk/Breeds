package com.alex.adapter.repository

import com.alex.domain.model.Breed
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface BreedRepository : CoroutineCrudRepository<Breed, Long> {
    suspend fun findBreedByName(name: String): Breed?

    @Query("SELECT name FROM Breed")
    suspend fun findAllBreedNames(): List<String>

    @Query("SELECT * FROM Breed LIMIT 1")
    suspend fun findFirst(): Breed?

}