import com.elbekD.bot.Bot


open class UflBotApplication
    fun main(args: Array<String>) {
        val token = "564873482:AAFhH9iE9Fyf1th-z-DSxznJBBgRSynPmvA"
        val bot = Bot.createPolling("" , token)

        bot.onCommand("/start") { msg, _ ->
                bot.sendMessage(msg.chat.id, "Hello World!")
        }
        bot.start()
    }

