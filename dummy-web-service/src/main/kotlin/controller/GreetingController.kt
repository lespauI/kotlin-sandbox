package kt.sandbox.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.atomic.AtomicLong
import kt.sandbox.data.Greeting


@RestController
class GreetingController {
    private val counter = AtomicLong()

    @GetMapping("/greeting")
    fun gritting(@RequestParam(value = "name", defaultValue = "world") name : String)
            = Greeting(counter.incrementAndGet(), "Hello $name")



}