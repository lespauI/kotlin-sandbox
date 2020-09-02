package functional

import com.jayway.jsonpath.JsonPath
import kt.sandbox.UflBotServiceApplication
import kt.sandbox.data.User

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.boot.test.web.client.TestRestTemplate

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.boot.test.web.client.getForObject
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.test.context.junit.jupiter.SpringExtension
import kotlin.test.assertEquals

@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = [UflBotServiceApplication::class], properties = ["management.server.port=8001"])
class UflBotServiceApplicationTests {

    @Value("\${management.server.port}")
    private val adminPort: Int? = null

    @Autowired
    private val restTemplate: TestRestTemplate? = null

    @Test
    fun contextLoads() {
    }

    @Test
    fun homeResponse() {
        val body = this.restTemplate!!.getForObject<String>("/", String::class.java)
        assertThat(body).isEqualTo("Spring is here!")
    }

    @Test
    fun testPing() {
        val body = this.restTemplate!!.getForObject<String>("http://localhost:$adminPort/admin/ping", String::class.java)
        assertThat(body).isEqualTo("pong")
    }

    @Test
    fun `get user list test`() {
        val body = restTemplate!!.getForObject<String>("/users", String)
        assertThat(body).contains("testUser1")
    }

    @Test @Disabled
    fun `add new user test`() {
        val user = User("qq", "ww")
        var response = restTemplate!!.exchange(
            "/user",
            HttpMethod.PUT,
            HttpEntity(user),
            String::class.java
        )
        assertEquals(HttpStatus.OK, response.statusCode)

        val body = restTemplate!!.getForObject<String>("/user/qq.json", String)
        assertThat(body).contains("ww")
    }
}
