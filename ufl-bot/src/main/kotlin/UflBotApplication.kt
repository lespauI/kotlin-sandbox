import com.elbekD.bot.Bot


open class UflBotApplication
    fun main(args: Array<String>) {
        val token = ""
        val bot = Bot.createPolling("" , token)

        bot.onCommand("/start") { msg, _ ->
                bot.sendMessage(msg.chat.id, "Hello World!")
        }
        bot.start()
    }

