package kt.sandbox

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication


@SpringBootApplication
open class UflBotServiceApplication

fun main(args: Array<String>) {
    SpringApplication.run(UflBotServiceApplication::class.java, *args)
}


