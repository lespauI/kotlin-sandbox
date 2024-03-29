package kt.sandbox.processor

import com.codeborne.selenide.Configuration
import com.codeborne.selenide.Selenide
import kt.sandbox.data.TradeBlock
import kt.sandbox.utils.Bot
import org.openqa.selenium.By
import org.openqa.selenium.OutputType.FILE
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.lang.Exception
import java.lang.RuntimeException

@Component
class DaddyProcessor @Autowired constructor(
        @Value("\${token}")
        val token: String,
        @Value("\${proxy.url}")
        val proxyUrl: String,
        @Value("\${proxy.port}")
        val proxyPort: Int,
        @Value("\${admin_chat_id}")
        val admin_chat_id: Long
) {

    val logger = LoggerFactory.getLogger(javaClass)

    val playerOverCss = ".pull-right.cfm-team-ovr"
    val home_page = "https://daddyleagues.com/uflrus"

    val bot = Bot(token)

    private var messages: ArrayList<String> = arrayListOf()

    fun parseMessage(chat_id: Long, body: Map<String, String>) {
        Configuration.browserSize = "1290x800"
        Configuration.headless = true

        if (System.getProperty("proxyEnabled", "false")!!.toBoolean()) {
            Configuration.proxyEnabled = true
            Configuration.proxyHost = proxyUrl
            Configuration.proxyPort = proxyPort
        }

        logger.info(body.toString())

        var message = body["content"].orEmpty()
        if (message == "")
            message = body["text"].orEmpty()
        logger.info("message = $message")
        message = message.replace("http://", "https://")
        var status = false

        if (messages.contains(message)) {
            //todo
            sendDebug(message + "\n#e100 Row is duplicated")
        }
        else if (body["type"].equals("tradeblock")) {
            messages.add(message)
            executeTradeBlock(chat_id, body)

        } else {
            messages.add(message)
            try {
                status = executeAction(chat_id, message)
                Selenide.closeWebDriver()
            } catch (re: RuntimeException) {
                var count = 0
                while (count < 10 && !status) {
                    Thread.sleep(30000)
                    try {
                        count++
                        status = executeAction(chat_id, message)
                    } catch (e: Exception) {
                    }
                    Selenide.closeWebDriver()
                }
            } catch (e: Exception) {
                Selenide.closeWebDriver()
                throw e
            }

            if (!status) {
                Selenide.closeWebDriver()
                sendDebug(message + "\n#e000 something goes wrong")
                throw UnknownError("XZ")
            }
            if (messages.size >= 100)
                messages.removeAll(messages.subList(0, messages.size - 30))
        }
    }

    private fun executeAction(chat_id: Long, message: String): Boolean {
        val link = message.replaceBefore("https://", "")
        when {
            message.contains("gamerecap") -> {
                //TODO refactor
                logger.info("link = $link")
                Configuration.browserSize = "1288x538"
                Selenide.open(link)
                //TODO add normal wait
                Thread.sleep(6000)
                bot.sendPic(
                        chat_id,
                        Selenide.element(By.cssSelector("#gamesummary")).getScreenshotAs(FILE),
                        "$message #score"
                )
                return true
            }
            message.contains("advanced to week") -> {
                //getSchedules(chat_id, message)

                clearCache()
                Thread.sleep(5000)
                Selenide.open(home_page)
                Thread.sleep(2000)

                bot.sendPic(
                        chat_id, Selenide.element(By.cssSelector(".card-body.p-0"), 5)
                        .getScreenshotAs(FILE), "Игрок нападения"
                )

                bot.sendPic(
                        chat_id, Selenide.element(By.cssSelector(".card-body.p-0"), 6)
                        .getScreenshotAs(FILE), "Игрок защиты"
                )

                getGameOfTheWeek(chat_id)

                bot.sendText(chat_id, "$message #шаг")
                return true
            }
            message.contains(Regex("Released|Signed")) -> {
                var gif = ""
                when {
                    message.contains("Released") -> gif =
                            "https://media.giphy.com/media/QVJanBtVwKFSFxxz3Z/giphy.gif"
                    message.contains("Signed") -> gif = "https://media.giphy.com/media/KHVexxqBrUvAfjhfAg/giphy.gif"
                }
                Selenide.open(link)
                Thread.sleep(6000)
                var over = Selenide.element(By.cssSelector(playerOverCss)).text()
                if (Integer.parseInt(over.dropLast(4)) >= 74) {
                    bot.sendAnimation(chat_id, gif, "$message \n$over #transaction")
                }
                return true
            }

            message.toLowerCase().contains("trade") -> {

                if (message.contains("submitted")) {
/*
                    bot.sendPic(
                            //admin_chat_id,
                            chat_id,
                            Selenide.element(By.cssSelector(".col-xl-10 .row")).getScreenshotAs(FILE),
                            "$message #trade"
                    )
                    bot.sendPool(
                            chat_id, "Одобряем?", "Да", "Нет", "Даю больше!"
                    )
*/
                }
                if (message.contains("approved")) {
                    login()
                    Configuration.browserSize = "1288x1888"
                    Selenide.open(link)
                    Thread.sleep(6000)
                    bot.sendPic(
                            -1001275286257,
                            //-273770462,
                            Selenide.element(By.cssSelector(".col-xl-10 .row")).getScreenshotAs(FILE),
                            "$message #trade"
                    )
                }
                return true
            }

            else -> {
                bot.sendText(-273770462, message)
                return true
            }
        }
    }

    private fun executeTradeBlock(chat_id: Long, body: Map<String, String>): Boolean {

        val tradeObj = TradeBlock(body.getOrDefault("text", ""),
                body.getOrDefault("player", ""),
                body.getOrDefault("team", ""),
                body.getOrDefault("age", ""),
                body.getOrDefault("ovr", ""),
                body.getOrDefault("price", ""),
                body.getOrDefault("pick", ""),
                body.getOrDefault("dev", ""),
                body.getOrDefault("link", ""),
                body.getOrDefault("pos", "")
        )
        var msg = ""
        if (tradeObj.player.isNotEmpty()) {
            msg = "*${tradeObj.team}:* #${tradeObj.pos}, [${tradeObj.player}](${tradeObj.link}) ${tradeObj.ovr} OVR\n" +
                    "*Age:* ${tradeObj.age}\n" +
                    "*Dev. Trait:* #${tradeObj.dev}\n" +
                    "*Needs:* ${tradeObj.price}\n" +
                    "#трейдблок"
        }
        if (tradeObj.pick.isNotEmpty()) {
            msg = "*${tradeObj.team}:* `${tradeObj.pick}`\n" +
                    "*Needs:* ${tradeObj.price}\n" +
                    "#пики"
        }
        bot.sendText(chat_id, "$msg #${tradeObj.team}")
        return true
    }


    private fun clearCache() {
        login()
        Selenide.open("$home_page/admin")
        Selenide.element(".btn.btn-info.ajax").click()
        Selenide.closeWebDriver()
    }

    private fun login() {
        try {
            Selenide.open("https://www.daddyleagues.com/login")
            Selenide.element("#username").sendKeys("lespaul1488")
            Selenide.element("#password").sendKeys("demonvanal")
            Selenide.element("#loginForm > button").click()
        } catch (re: RuntimeException) {
            throw re
        } catch (e: NoSuchElementException) {
            println("kjhkhjkjh")
        }
    }

    fun getSchedules(chat_id: Long, msg: String) {
        Configuration.browserSize = "1288x1288"
        Selenide.open("$home_page/schedules")
        bot.sendPic(
                chat_id,
                Selenide.element(By.cssSelector("#scores")).getScreenshotAs(FILE),
                msg
        )
    }

    fun getGameOfTheWeek(chat_id: Long) {
        Selenide.open(home_page)
        Thread.sleep(6000)
        bot.sendPic(
                chat_id,
                Selenide.element(By.cssSelector("#weekgame")).getScreenshotAs(FILE),
                "Главная игра недели"
        )

        val guest: String = Selenide.element(".gameoftheweek > .row.row-flush > div:nth-child(1)")
                .getAttribute("style")
                .replace(Regex("(.*)/left/(\\d+).png.*"), "$2")
        val home: String = Selenide.element(".gameoftheweek > .row.row-flush > div:nth-child(4)")
                .getAttribute("style")
                .replace(Regex("(.*)/right/(\\d+).png.*"), "$2")

        bot.sendPool(
                chat_id, "Кто победит?", teamList[Integer.parseInt(guest)],
                teamList[Integer.parseInt(home)]
        )

    }

    fun rage(chat_id: Long) {
        bot.sendAnimation(chat_id, "https://media.giphy.com/media/EtB1yylKGGAUg/giphy.gif", "")
    }

    fun getMessagesList(): ArrayList<String> {
        return messages
    }

    fun sendDebug(msg: String) {
        bot.sendFail(-273770462, "\n$msg\n")
    }


    val teamList = listOf(
            "Bears", "Bengals", "Bills", "Broncos", "Browns", "Buccaneers", "Cardinals", "Chargers", "Chiefs",
            "Colts", "Cowboys", "Dolphins", "Eagles", "Falcons", "49ers", "Giants", "Jaguars", "Jets", "Lions",
            "Packers", "Panthers", "Patriots", "Raiders", "Rams", "Ravens", "Football Team", "Saints", "Seahawks",
            "Steelers", "Titans", "Vikings", "Texans"
    )
}
