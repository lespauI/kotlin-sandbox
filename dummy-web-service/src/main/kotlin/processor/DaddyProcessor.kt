package kt.sandbox.processor

import com.codeborne.selenide.Configuration
import com.codeborne.selenide.Screenshots
import com.codeborne.selenide.Selenide
import kt.sandbox.utils.Bot
import org.openqa.selenium.By
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

    var gameId: String = ""
    val downloadBtn = ".btn.btn-primary.downloadrecap.text-white"
    val playerOverCss = ".pull-right.cfm-team-ovr"
    var link = "http://daddyleagues.com/uflrus/"

    var bot = Bot(token)

    fun parseMessage(chat_id: Long, body: Map<String, String>) {

        logger.info(body.toString())

        var message = body.get("content").orEmpty()
        if (message == "")
            message = body.get("text").orEmpty()
        logger.info("message = $message")

        when {
            message.contains("gamerecap") -> {
                //TODO refactor
                link = message.replaceBefore("http://", "")
                this.gameId = message.replaceBefore("/gamerecap/", "")
                    .replace("/gamerecap/", "")
                logger.info("link = $link")
                logger.info("gameId = $gameId")
                Selenide.open(link)
                Selenide.element(By.cssSelector(downloadBtn)).click()
                bot.sendGameRecap(chat_id, gameId, message)
                Selenide.closeWebDriver()
            }
            message.contains("advanced to week") -> {
                try {
                    Configuration.browserSize = "350x620"
                    Selenide.open(link)
                    Selenide.element(By.cssSelector("#weekgame")).scrollTo()
                    bot.sendPic(chat_id, Screenshots.takeScreenShotAsFile(), message)
                } catch (e: Exception) {
                    bot.sendFail(chat_id, message)
                    throw e
                } finally {
                    Selenide.closeWebDriver()
                }
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
                Selenide.closeWebDriver()
            }
            else -> {
                bot.sendText(chat_id, message)
            }

        }

    }

    fun rage(chat_id: Long) {
        bot.sendAnimation(chat_id, "https://media.giphy.com/media/EtB1yylKGGAUg/giphy.gif", "")

    }
}