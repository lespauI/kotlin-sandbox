package kt.sandbox.controller

import org.springframework.boot.actuate.endpoint.annotation.Endpoint
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.config.annotation.EnableWebMvc

@Configuration
@EnableWebMvc
open class AdminEndpoints {

    @Component
    @Endpoint(id = "ping")
    class PingEndpoint {
        @ReadOperation
        fun pingPong() = "pong"
    }
}