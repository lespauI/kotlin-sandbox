package kt.sandbox.utils

import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.request.SendAnimation
import com.pengrad.telegrambot.request.SendMessage
import com.pengrad.telegrambot.request.SendPhoto
import org.slf4j.LoggerFactory
import org.springframework.util.ResourceUtils
import java.io.File
import java.io.InputStream

class Bot(token: String?) {

    val logger = LoggerFactory.getLogger(javaClass)

    val path_to_recap = "build/downloads"

    var bot = TelegramBot(token)

    fun sendGameRecap(chatId: Long, gameId: String, message: String) {
        var file = File("$path_to_recap/$gameId.png")
        val sendPhoto = SendPhoto(chatId, file).caption(message)
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
            else {
                file = ResourceUtils.getFile("classpath:gifs/fail.gif")
                bot.execute(SendAnimation(chatId, file).caption(message))
            }
        }
    }

    fun sendText(chatId: Long, message: String) {
        bot.execute(SendMessage(chatId, message))
    }

    fun sendAnimation(chatId: Long, animation_url: String, caption: String) {
        bot.execute(SendAnimation(chatId, animation_url).caption(caption))
    }
    fun sendLocalAnimation(chatId: Long, fileName: String, caption: String) {
        val file = ResourceUtils.getFile("classpath:$fileName")
        bot.execute(SendAnimation(chatId, file).caption(caption))
    }
}