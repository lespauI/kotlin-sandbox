package kt.sandbox

import com.elbekD.bot.Bot
import org.springframework.boot.autoconfigure.SpringBootApplication
import kt.sandbox.bot.AdminController


@SpringBootApplication
open class UflBotApplication

fun main(args: Array<String>) {

    val adminController =  AdminController()

    val token = System.getProperty("token")
    val bot = Bot.createPolling("", token)

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

    bot.onCommand("/waiver") { msg, _ ->
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

    bot.onCommand("/elo") { msg, _ ->
        bot.sendMessage(msg.chat.id, "Executing elo")
        /* methods for waiver logic
            1. гет эло
            2. сет эло
            3. апдейт эло
            4. флюш эло

            все начинают с 1200 эло
        */
    }

    bot.onCommand("/service") {msg, _->
        bot.sendMessage(msg.chat.id, adminController.serviceStatus())
    }

    bot.start()
}
