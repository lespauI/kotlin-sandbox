package kt.sandbox.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class DefaultController {

    @GetMapping("/")
    fun default() = "Spring is here!"

}
