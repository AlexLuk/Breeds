import com.alex.adapter.client.BreedClient
import com.alex.config.properties.BreedClientProperties
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import kotlin.test.assertEquals

class BreedClientTest {

    private val properties: BreedClientProperties = BreedClientProperties("https://siteUrl.com")
    private val webClient: WebClient = mockk()

    private val breedClient: BreedClient = BreedClient(properties, webClient)

    @Test
    fun shouldGetImageByBreedName(): Unit = runBlocking {
        //given
        val breedName = "breedName"
        val breedNameImage = "breedNameImage"
        val byteArray = byteArrayOf(1, 2, 3)
        val externalImageDto = BreedClient.ExternalImageDto(listOf(breedNameImage), "", null)
        val randomImageLinkMono: Mono<ResponseEntity<BreedClient.ExternalImageDto>> =
            Mono.just(ResponseEntity(externalImageDto, HttpStatus.OK))
        val imageDownloadMono = Mono.just(ResponseEntity(byteArray, HttpStatus.OK))

        coEvery {
            webClient.get().uri(any<String>()).retrieve().toEntity(BreedClient.ExternalImageDto::class.java)
        } returns randomImageLinkMono
        coEvery {
            webClient.get().uri(any<String>()).retrieve().toEntity(ByteArray::class.java)
        } returns imageDownloadMono

        //when
        val result = breedClient.getImage(breedName)

        //then
        assertEquals(breedName, result.name)
        assertEquals(byteArray, result.content)
    }
}