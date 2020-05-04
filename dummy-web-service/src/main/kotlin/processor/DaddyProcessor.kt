package kt.sandbox.processor

import com.codeborne.selenide.Selenide
import kt.sandbox.utils.Bot
import org.openqa.selenium.By
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class DaddyProcessor @Autowired constructor(
    @Value("\${token}")
    val token: String
) {

    val logger = LoggerFactory.getLogger(javaClass)

    var gameId: String = ""
    var downloadBtn = ".btn.btn-primary.downloadrecap.text-white"

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
                val link = message.replaceBefore("http://", "")
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
                bot.sendLocalAnimation(chat_id, "dummy-web-service/gifs/advanced.mp4", message)
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