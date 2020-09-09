package kt.sandbox

import org.springframework.boot.autoconfigure.SpringBootApplication


@SpringBootApplication
open class UflBotApplication(
)

fun main(args: Array<String>) {

    Data.bot.Bot().start()

}
