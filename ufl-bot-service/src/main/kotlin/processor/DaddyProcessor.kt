package kt.sandbox.processor

import com.codeborne.selenide.Configuration
import com.codeborne.selenide.Selenide
import kt.sandbox.utils.Bot
import org.openqa.selenium.By
import org.openqa.selenium.OutputType.FILE
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.lang.Exception

@Component
class DaddyProcessor @Autowired constructor(
    @Value("\${token}")
    val token: String
) {

    val logger = LoggerFactory.getLogger(javaClass)

    val playerOverCss = ".pull-right.cfm-team-ovr"
    val link = "http://daddyleagues.com/uflrus"

    val bot = Bot(token)

    fun parseMessage(chat_id: Long, body: Map<String, String>) {
        Configuration.browserSize = "1290x800"
        logger.info(body.toString())

        var message = body.get("content").orEmpty()
        if (message == "")
            message = body.get("text").orEmpty()
        logger.info("message = $message")
        var status = false

        try {
            status = executeAction(chat_id, message)
            Selenide.closeWebDriver()

        } catch (e: Exception) {
            bot.sendFail(
                -273770462,
                "Error for\n$message\n" +
                        "$e \n " +
                        " Refresh by https://daddy-league-new-staging.herokuapp.com/daddyleague/push"
            )
            Selenide.closeWebDriver()
            throw e
            }

        if (!status) {
            Selenide.closeWebDriver()
            bot.sendFail(
                -273770462,
                "Error for\n$message\n" +
                        "XZ \n " +
                        " Refresh by https://daddy-league-new-staging.herokuapp.com/daddyleague/push"
            )
            throw UnknownError("XZ")
        }
    }

    private fun executeAction(chat_id: Long, message: String): Boolean {
        when {
            message.contains("gamerecap") -> {
                //TODO refactor
                val link = message.replaceBefore("http://", "")
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
                Selenide.open(link)
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
                val link = message.replaceBefore("http://", "")
                Selenide.open(link)
                Thread.sleep(6000)
                var over = Selenide.element(By.cssSelector(playerOverCss)).text()
                if (Integer.parseInt(over.dropLast(4)) >= 74) {
                    bot.sendAnimation(chat_id, gif, "$message \n$over #transaction")
                }
                return true
            }
            else -> {
                bot.sendText(chat_id, message)
                return true
            }
        }
    }

    private fun clearCache() {
        Selenide.open("http://www.daddyleagues.com/login")
        Selenide.element("#username").sendKeys("lespaul1488")
        Selenide.element("#password").sendKeys("ZuZkezW7")
        Selenide.element("#loginForm > button").click()
        Selenide.open("$link/admin")
        Selenide.element(".btn.btn-info.ajax").click()
        Selenide.closeWebDriver()
    }


    fun getSchedules(chat_id: Long, msg: String) {
        Configuration.browserSize = "1288x1288"
        Selenide.open("$link/schedules")
        bot.sendPic(
            chat_id,
            Selenide.element(By.cssSelector("#scores")).getScreenshotAs(FILE),
            msg
        )
    }

    fun getGameOfTheWeek(chat_id: Long) {
        Selenide.open(link)
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

    fun sendDebug(msg: String, exception: java.lang.Exception) {
        //bot.sendFail(-273770462, "Error for\n$msg\n$exception")
        bot.sendFail(
            -273770462,
            "\n$msg\n" +
                    "\nError - \n$exception"
                    + "\nRefresh by https://daddy-league-new-staging.herokuapp.com/daddyleague/push"
        )
    }


    val teamList = listOf(
        "Bears", "Bengals", "Bills", "Broncos", "Browns", "Buccaneers", "Cardinals", "Chargers", "Chiefs",
        "Colts", "Cowboys", "Dolphins", "Eagles", "Falcons", "49ers", "Giants", "Jaguars", "Jets", "Lions",
        "Packers", "Panthers", "Patriots", "Raiders", "Rams", "Ravens", "Redskins", "Saints", "Seahawks",
        "Steelers", "Titans", "Vikings", "Texans"
    )
}