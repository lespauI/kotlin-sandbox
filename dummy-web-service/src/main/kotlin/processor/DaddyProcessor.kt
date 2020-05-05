package kt.sandbox.processor

import com.codeborne.selenide.Configuration
import com.codeborne.selenide.Screenshots
import com.codeborne.selenide.Selenide
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
    val link = "http://daddyleagues.com/uflrus/"

    val bot = Bot(token)

    fun parseMessage(chat_id: Long, body: Map<String, String>) {
        Configuration.browserSize = "1290x620"
        Configuration.browserCapabilities.setCapability("arguments","--hide-scrollbars")
        logger.info(body.toString())

        var message = body.get("content").orEmpty()
        if (message == "")
            message = body.get("text").orEmpty()
        logger.info("message = $message")

        when {
            message.contains("gamerecap") -> {
                //TODO refactor
                val link = message.replaceBefore("http://", "")
                logger.info("link = $link")
                Configuration.browserSize = "1290x620"
                Selenide.open(link)
                bot.sendPic(chat_id,
                    Selenide.element(By.cssSelector("#gamerecapdownload")).getScreenshotAs(FILE),
                    message)
                Selenide.closeWebDriver()
                //bot.sendGameRecap(chat_id, gameId, message)
            }
            message.contains("advanced to week") -> {
                try {
                    Configuration.browserSize = "450x550"
                    Selenide.open(this.link)
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