package com.alex.adapter.rest.advice

import com.alex.application.service.exception.BreedImageNotFoundException
import com.alex.application.service.exception.BreedNotFoundException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpEntity
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

private val log: org.slf4j.Logger = LoggerFactory.getLogger(ExceptionControllerAdvice::class.java)

@ControllerAdvice
class ExceptionControllerAdvice {
    @ExceptionHandler(BreedImageNotFoundException::class)
    fun handleBreedImageNotFoundException(e: BreedImageNotFoundException): HttpEntity<ErrorResponse> {
        val detail = "Breed image not found for breed name ${e.breedName}"
        log.error(detail)
        return responseEntity(detail)
    }

    @ExceptionHandler(BreedNotFoundException::class)
    fun handleBreedNotFoundException(e: BreedNotFoundException): HttpEntity<ErrorResponse> {
        val detail = "Breed not found for breed name ${e.breedName}"
        log.error(detail, e)
        return responseEntity(detail)
    }

    private fun responseEntity(detail: String) =
        ResponseEntity(ErrorResponse(detail), HttpStatus.NOT_FOUND)

    data class ErrorResponse(val detail: String)
}
