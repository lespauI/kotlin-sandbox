package utils

import com.elbekD.bot.Bot
import com.elbekD.bot.types.CallbackQuery
import com.elbekD.bot.types.InlineKeyboardButton
import com.elbekD.bot.types.InlineKeyboardMarkup
import data.Voters
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class VoteProcessor @Autowired constructor() {

    private var voterList = mutableMapOf<Voters, String>()

    fun vote(it: CallbackQuery, bot: Bot, type: String, buttonId: Int) {

        val voter = Voters(it.from.id, it.message!!.message_id, it.message!!.chat.id)

        var like = Integer.parseInt(
                it.message!!.reply_markup!!.inline_keyboard[0][buttonId].text.split(" ")[1]
        )

        when {
            voterList[voter].isNullOrEmpty() -> {
                if(voterList.size > 1000) {
                    voterList.clear()
                }
                like += 1
                voterList[voter] = type
            }
            voterList[voter] == type -> {
                like -= 1
                voterList.remove(voter)
            }
            else -> {
                bot.answerCallbackQuery(it.id, "Already voted for \"${voterList[voter]}\"", false)
            }
        }

        bot.editMessageReplyMarkup(
                chatId = it.message?.chat?.id, messageId = it.message?.message_id,
                markup = InlineKeyboardMarkup(listOf(getInlineKeyboardButtonList(type, like, it))))

    }

    private fun getInlineKeyboardButtonList(type: String, like: Int, it: CallbackQuery): List<InlineKeyboardButton> {
        return when (type) {
            "like" -> {
                return listOf(
                        InlineKeyboardButton("👍 $like", callback_data = type),
                        it.message!!.reply_markup!!.inline_keyboard[0][1],
                        it.message!!.reply_markup!!.inline_keyboard[0][2]
                )
            }
            "dislike" -> {
                return listOf(
                        it.message!!.reply_markup!!.inline_keyboard[0][0],
                        InlineKeyboardButton("👎 $like", callback_data = type),
                        it.message!!.reply_markup!!.inline_keyboard[0][2]
                )
            }
            "bue" -> {
                return listOf(
                        it.message!!.reply_markup!!.inline_keyboard[0][0],
                        it.message!!.reply_markup!!.inline_keyboard[0][1],
                        InlineKeyboardButton("🤢 $like", callback_data = type)
                )
            }
            else -> return listOf(
                    it.message!!.reply_markup!!.inline_keyboard[0][0],
                    it.message!!.reply_markup!!.inline_keyboard[0][1],
                    it.message!!.reply_markup!!.inline_keyboard[0][2]
            )
        }
    }
}