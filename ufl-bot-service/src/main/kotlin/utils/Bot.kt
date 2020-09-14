package kt.sandbox.utils

import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.request.InlineKeyboardButton
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup
import com.pengrad.telegrambot.request.SendAnimation
import com.pengrad.telegrambot.request.SendMessage
import com.pengrad.telegrambot.request.SendPhoto
import com.pengrad.telegrambot.request.SendPoll
import org.slf4j.LoggerFactory
import java.io.File


class Bot(token: String?) {

    val logger = LoggerFactory.getLogger(javaClass)

    val path_to_recap = "build/downloads"

    var bot = TelegramBot(token)

    @Deprecated("to remove")
    fun sendGameRecap(chat_id: Long, gameId: String, message: String) {
        var file = File("$path_to_recap/$gameId.png")
        val sendPhoto = SendPhoto(chat_id, file).caption(message)
        bot.execute(sendPhoto)
        file.delete()
    }

    fun sendText(chat_id: Long, message: String) {
        bot.execute(SendMessage(chat_id, message).disableWebPagePreview(true))
    }

    fun sendAnimation(chat_id: Long, animation_url: String, caption: String) {
        bot.execute(SendAnimation(chat_id, animation_url).caption(caption))
    }

    fun sendPic(chat_id: Long, file: File, caption: String) {
        var mrk = InlineKeyboardMarkup(
            arrayOf(
                InlineKeyboardButton("👍 0").callbackData("like"),
                InlineKeyboardButton("👎 0").callbackData("dislike"),
                InlineKeyboardButton("🤢 0").callbackData("bue")
            )
        )
        bot.execute(SendPhoto(chat_id, file).caption(caption).replyMarkup(mrk))
    }

    fun sendPool(chat_id: Long, question: String, vararg options: String) {
        bot.execute(SendPoll(chat_id, question, *options).isAnonymous(false))
    }

    fun sendFail(chat_id: Long, message: String) {
        bot.execute(
            SendAnimation(
                chat_id,
                "https://media.giphy.com/media/Sv9XNImzlvO2JuSnMk/giphy.gif"
            ).caption(message)
        )
    }


}