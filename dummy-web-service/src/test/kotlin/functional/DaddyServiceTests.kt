package functional

import kt.sandbox.DummyServiceApplication
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.test.context.junit.jupiter.SpringExtension
import kotlin.test.assertEquals


@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = [DummyServiceApplication::class], properties = ["management.server.port=8001"])
class DaddyServiceTests(
    @Autowired
    private val restTemplate: TestRestTemplate
) {

    val endpoint = "/daddyleague/"

    @Test
    fun `Get rage gif test`() {
        var response = restTemplate.exchange(
            "$endpoint/rage/-273770462",
            HttpMethod.GET,
            null,
            String::class.java
        )
        assertEquals(HttpStatus.OK, response.statusCode)

        //Assertions.assertThat(body).contains("ww")
    }

}