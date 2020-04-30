package kt.sandbox

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication


@SpringBootApplication
open class DummyServiceApplication

fun main(args: Array<String>) {
    SpringApplication.run(DummyServiceApplication::class.java, *args);
}


