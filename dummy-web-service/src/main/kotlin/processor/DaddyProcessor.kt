package kt.sandbox.processor

import com.codeborne.selenide.Configuration
import com.codeborne.selenide.Screenshots
import com.codeborne.selenide.Selenide
import kt.sandbox.data.Team
import kt.sandbox.utils.Bot
import org.openqa.selenium.By
import org.openqa.selenium.OutputType.FILE
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.remote.DesiredCapabilities
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.io.File
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

        try {
            when {
                message.contains("gamerecap") -> {
                    //TODO refactor
                    val link = message.replaceBefore("http://", "")
                    logger.info("link = $link")
                    Configuration.browserSize = "1288x538"
                    Selenide.open(link)
                    //TODO add normal wait
                    Thread.sleep(2000)
                    sendItemAsPic(chat_id, "#gamesummary", message)
                }
                message.contains("advanced to week") -> {

                    //getSchedules(chat_id, message)
                    bot.sendText(chat_id, message)

                    Selenide.open(link)
                    //sendItemAsPic(chat_id, "#weekplayer", "Players Of the Week")
                    bot.sendPic(
                        chat_id, Selenide.element(By.cssSelector(".card-body.p-0"), 5)
                            .getScreenshotAs(FILE), "Игрок нападения"
                    )

                    bot.sendPic(
                        chat_id, Selenide.element(By.cssSelector(".card-body.p-0"), 6)
                            .getScreenshotAs(FILE), "Игрок защиты"
                    )

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
                    var over = Selenide.element(By.cssSelector(playerOverCss)).text()
                    if (Integer.parseInt(over.dropLast(4)) >= 74) {
                        bot.sendAnimation(chat_id, gif, "$message \n$over")
                    }
                }
                else -> {
                    bot.sendText(chat_id, message)
                }
            }
        } catch (e: Exception) {
            bot.sendFail(chat_id, message)
            throw e
        } finally {
            Selenide.closeWebDriver()
        }

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

    fun sendItemAsPic(chat_id: Long, cssSelector: String, caption: String) {
        bot.sendPic(
            chat_id, Selenide.element(By.cssSelector(cssSelector)).getScreenshotAs(FILE), caption
        )
    }

    fun getGameOfTheWeek(chat_id: Long) {
        Selenide.open(link)
        Thread.sleep(2000)
        sendItemAsPic(chat_id, "#weekgame", "Главная игра недели")

        val guest: String = Selenide.element(".gameoftheweek > .row.row-flush > div:nth-child(1)")
            .getAttribute("style")
            .replace(Regex("(.*)/left/(\\d+).png.*"), "$2")
        val home: String = Selenide.element(".gameoftheweek > .row.row-flush > div:nth-child(4)")
            .getAttribute("style")
            .replace(Regex("(.*)/right/(\\d+).png.*"), "$2")

        bot.sendPool(chat_id, "Кто победит?", teamList[Integer.parseInt(guest)],
            teamList[Integer.parseInt(home)])
        Selenide.closeWebDriver()
    }

    fun rage(chat_id: Long) {
        bot.sendAnimation(chat_id, "https://media.giphy.com/media/EtB1yylKGGAUg/giphy.gif", "")
    }

    val teamList = listOf(
        "Bears", "Bengals", "Bills", "Broncos", "Browns", "Buccaneers", "Cardinals", "Chargers", "Chiefs",
        "Colts", "Cowboys", "Dolphins", "Eagles", "Falcons", "49ers", "Giants", "Jaguars", "Jets", "Lions",
        "Packers", "Panthers", "Patriots", "Raiders", "Rams", "Ravens", "Redskins", "Saints", "Seahawks",
        "Steelers", "Titans", "Vikings", "Texans"
    )
}