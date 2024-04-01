    package com.alex.adapter.client

    import com.alex.application.service.dto.ImageDto
    import com.alex.application.service.exception.BreedImageNotFoundException
    import com.alex.config.properties.BreedClientProperties
    import kotlinx.coroutines.reactive.awaitFirstOrNull
    import org.slf4j.LoggerFactory
    import org.springframework.http.HttpStatus
    import org.springframework.stereotype.Service
    import org.springframework.web.client.HttpClientErrorException
    import org.springframework.web.reactive.function.client.WebClient
    import org.springframework.web.util.UriComponentsBuilder
    import java.io.IOException


    private val log: org.slf4j.Logger = LoggerFactory.getLogger(BreedClient::class.java)

    @Service
    class BreedClient(
        private val properties: BreedClientProperties,
        private val webClient: WebClient
    ) {
        private final val allBreedsPath: String = "/breeds/list/all"
        private final val breedNamePathVariable = "breedName"
        private val images: String = "/breed/{$breedNamePathVariable}/images"
        suspend fun getImage(breedName: String): ImageDto {
            log.info("Searching for images for breed $breedName")
            val randomImageLink = getRandomImageLink(breedName)
            log.info("Image was downloaded for breed $breedName")
            return ImageDto(downloadImage(randomImageLink), breedName)
        }

        private suspend fun downloadImage(url: String): ByteArray {
            try {
                val responseSpec = webClient.get()
                    .uri(url)
                    .retrieve()
                val responseEntitySpec = responseSpec.toEntity(ByteArray::class.java)
                val response = responseEntitySpec.awaitFirstOrNull()
                if (response?.statusCode == HttpStatus.OK) {
                    return response.body ?: throw IOException("Empty image response")
                } else {
                    throw HttpClientErrorException(
                        response?.statusCode ?: HttpStatus.INTERNAL_SERVER_ERROR,
                        "Failed to download image: ${response?.statusCode}"
                    )
                }
            } catch (e: Exception) {
                throw IOException("Failed to download image: ${e.message}", e)
            }
        }

        private suspend fun getRandomImageLink(breedName: String): String {
            val url = UriComponentsBuilder.fromHttpUrl(properties.url).path(images)
                .uriVariables(mapOf(breedNamePathVariable to breedName))
                .toUriString()
            val responseSpec = webClient.get()
                .uri(url)
                .retrieve()
            val responseEntitySpec = responseSpec.toEntity(ExternalImageDto::class.java)
            val response = responseEntitySpec.awaitFirstOrNull()
            val result = response?.body?.message ?: emptyList()
            return result.randomOrNull() ?: throw BreedImageNotFoundException(breedName)
        }

        suspend fun getAllBreeds(): Set<String> {
            log.info("Looking for breeds")
            val url = properties.url + allBreedsPath
            val responseSpec = webClient.get()
                .uri(url)
                .retrieve()
            val responseEntitySpec = responseSpec.toEntity(ExternalBreedDto::class.java)
            val response = responseEntitySpec.awaitFirstOrNull()
            val breeds = response?.body?.message ?: emptyMap()
            log.info("Number of basic breeds found: ${breeds.keys.size} ")
            val result = breeds.entries.map { (breed, subBreeds) ->
                if (subBreeds.isEmpty()) {
                    listOf(breed)
                } else {
                    subBreeds.map { subBreed -> "$subBreed $breed" }.toList()
                }
            }.flatten().toSet()
            log.info("Number of individual breeds combined: ${result.size} ")
            return result
        }

        data class ExternalBreedDto(val message: Map<String, List<String>>, val status: String, val code: Int?)
        data class ExternalImageDto(val message: List<String>, val status: String, val code: Int?)


    }