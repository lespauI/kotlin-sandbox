import com.elbekD.bot.Bot
import org.springframework.boot.autoconfigure.SpringBootApplication


@SpringBootApplication
open class UflBotApplication

fun main(args: Array<String>) {
    val token = System.getProperty("token")
    val bot = Bot.createPolling("", token)

    bot.onCommand("/start") { msg, _ ->
        bot.sendMessage(msg.chat.id, "Hello World!")
    }
    bot.start()
}
