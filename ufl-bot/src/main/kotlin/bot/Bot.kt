package Data.bot

import com.elbekD.bot.Bot
import com.elbekD.bot.types.InlineKeyboardButton
import com.elbekD.bot.types.InlineKeyboardMarkup
import kt.sandbox.data.Elo
import kt.sandbox.utils.database.DbConnector
import kt.sandbox.utils.database.DbProcessor
import java.util.*


open class Bot {


    val dbConnector = DbConnector()

    val adminController = AdminController()

    val token = System.getProperty("token")
    val bot = Bot.createPolling("newUflBot", token)

    fun start() {
        bot.onCommand("/start") { msg, _ ->
            bot.sendMessage(msg.chat.id, "Hello World!")
        }

        /* bot.onCommand("/set") { msg, _ ->
         bot.sendMessage(msg.chat.id, "Executing set")
         //set to database
     }

     bot.onCommand("/get") { msg, _ ->
         bot.sendMessage(msg.chat.id, "Executing get")
         //get from dbase
     }*/

        bot.onCommand("/waiver")
        { msg, _ ->
            bot.sendMessage(msg.chat.id, "Executing waiver")
            /* methods for waiver logic

            Объявляется вейвер со списком игроков
                1) возможность добавить список
                2) возможность посмотреть список (ссылки на дядю)
                3) возможность зааплаится на игрока
                    3.1 в базе создается связь владелец - игрок
                4) Список всех команд и их очередность на вейвере
                    4.1 возможность просмотреть
                    4 2 возможность редактировать
                5) лимит не более 2 вейверов в сезон
                6) победитель вейвера переходит в конец очереди
                7) Если следующая по приоритету команда хотела взять игрока, который ушел, то, соответственно, ей достается второй игрок из заявки

         */
        }

        bot.onCommand("/elo")
        { msg, _ ->
            /* methods for waiver logic
            1. гет эло
            2. сет эло
            3. апдейт эло
            4. флюш эло

            все начинают с 1200 эло
        */
            dbConnector.connect()
            val dbProcessor = DbProcessor(dbConnector.getConnection()!!)

            val rs = dbProcessor.executeQuery("Select name, elo from managers order by elo desc")

            val list: MutableList<String> = ArrayList<String>()

            while (rs?.next()!!) {
                list.add(Elo(rs.getInt("elo"), rs.getString("name")).toString())
            }

            bot.sendMessage(
                msg.chat.id, list.toString()
                    .replace(",", "")
                    .replace("[", "")
                    .replace("]", "")
            )


        }

        bot.onCommand("/service")
        { msg, _ ->
            bot.sendMessage(msg.chat.id, adminController.serviceStatus())
        }

        bot.onCommand("/debug")
        { msg, _ ->
            dbConnector.connect()
            val dbProcessor = DbProcessor(dbConnector.getConnection()!!)
            val teamsRS = dbProcessor.executeQuery("SELECT * FROM teams")
            val managersRS = dbProcessor.executeQuery("SELECT * FROM managers")


            println(teamsRS?.getString(1))
            println(managersRS?.getString(1))

        }

        bot.onCommand("/db_execute") { msg, _ ->
            dbConnector.connect()
            val dbProcessor = DbProcessor(dbConnector.getConnection()!!)

            val query: String = msg.text?.split(" ", limit = 2)?.get(1) ?: ""

            val teamsRS = dbProcessor.executeQuery(query.toString())

            println(teamsRS?.getString(2))
        }

        bot.onCallbackQuery("like") {
            var like = Integer.parseInt(
                it.message!!.reply_markup!!.inline_keyboard[0][0].text.split(" ")[1]
            )
            like += 1
            bot.editMessageReplyMarkup(
                chatId = it.message?.chat?.id, messageId = it.message?.message_id, markup = InlineKeyboardMarkup(
                    listOf(
                        listOf(
                            InlineKeyboardButton("👍 $like", callback_data = "like"),
                            it.message!!.reply_markup!!.inline_keyboard[0][1],
                            it.message!!.reply_markup!!.inline_keyboard[0][2]
                        )
                    )
                )
            )
        }
        bot.onCallbackQuery("dislike") {
            var like = Integer.parseInt(
                it.message!!.reply_markup!!.inline_keyboard[0][1].text.split(" ")[1]
            )
            like += 1
            bot.editMessageReplyMarkup(
                chatId = it.message?.chat?.id, messageId = it.message?.message_id, markup = InlineKeyboardMarkup(
                    listOf(
                        listOf(
                            it.message!!.reply_markup!!.inline_keyboard[0][0],
                            InlineKeyboardButton("👎 $like", callback_data = "dislike"),
                            it.message!!.reply_markup!!.inline_keyboard[0][2]
                        )
                    )
                )
            )
        }
        bot.onCallbackQuery("bue") {
            var like = Integer.parseInt(
                it.message!!.reply_markup!!.inline_keyboard[0][2].text.split(" ")[1]
            )
            like += 1
            bot.editMessageReplyMarkup(
                chatId = it.message?.chat?.id, messageId = it.message?.message_id, markup = InlineKeyboardMarkup(
                    listOf(
                        listOf(
                            it.message!!.reply_markup!!.inline_keyboard[0][0],
                            it.message!!.reply_markup!!.inline_keyboard[0][1],
                            InlineKeyboardButton("🤢 $like", callback_data = "bue")
                        )
                    )
                )
            )
        }

        bot.start()
    }
}