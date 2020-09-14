package kt.sandbox

import org.springframework.boot.autoconfigure.SpringBootApplication


@SpringBootApplication
open class UflBotApplication(
)

fun main(args: Array<String>) {

    data.bot.Bot().start()

}
