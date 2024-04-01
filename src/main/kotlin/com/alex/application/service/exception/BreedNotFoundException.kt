package com.alex.application.service.exception

class BreedNotFoundException(val breedName: String) : RuntimeException()