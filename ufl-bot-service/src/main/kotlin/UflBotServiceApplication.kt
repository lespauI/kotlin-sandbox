package kt.sandbox

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.annotation.EnableScheduling


@SpringBootApplication
/*@EnableScheduling
@EnableAsync*/
open class UflBotServiceApplication

fun main(args: Array<String>) {
    SpringApplication.run(UflBotServiceApplication::class.java, *args)
}


