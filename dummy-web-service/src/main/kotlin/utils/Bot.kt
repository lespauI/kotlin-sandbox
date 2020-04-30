package kt.sandbox.utils

import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.request.SendMessage
import com.pengrad.telegrambot.request.SendPhoto
import java.io.File

class Bot(token: String?) {

    val path_to_recap = "build/downloads"

    var bot = TelegramBot(token)

    fun sendPhoto(chatId: Long, gameId: String) {
        val file = File("$path_to_recap/$gameId.png")
        val sendPhoto = SendPhoto(chatId, file)
        if (file.exists())
            try {
                bot.execute(sendPhoto)
            } finally {
                file.delete()
            }
        else {
            Thread.sleep(10000)
            if(file.exists())
                try {
                    bot.execute(sendPhoto)
                } finally {
                    file.delete()
                }
            else bot.execute(SendMessage(chatId, "GameRecap не доступен :("))
        }
    }

    fun sendText(chatId: Long, message: String) {
        bot.execute(SendMessage(chatId, message))
    }
}