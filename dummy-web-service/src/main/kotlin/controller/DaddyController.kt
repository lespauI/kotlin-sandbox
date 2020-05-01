package kt.sandbox.controller

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

import com.codeborne.selenide.Selenide.*
import kt.sandbox.utils.Bot
import kt.sandbox.utils.UserHolder
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.PathVariable
import javax.websocket.server.PathParam

@RestController
class DaddyController {

    val logger = LoggerFactory.getLogger(javaClass)

    @Value("\${token}")
    val token: String? = ""

    var gameId: String = ""
    var downloadBtn = ".btn.btn-primary.downloadrecap.text-white"

    @PostMapping("/daddyleague/{chat_id}")
    fun postWebHook(@RequestBody body: Map<String, String>, @PathVariable chat_id: Long) {
        var bot = Bot(token)
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
                open(link)
                element(By.cssSelector(downloadBtn)).click()
                bot.sendText(chat_id, message)
                bot.sendPhoto(chat_id, gameId)
                closeWebDriver()
            }
            else -> {
                bot.sendText(chat_id, message)
            }
        }
    }
}


    /*
    {
  "content": " A new article has been posted http://daddyleagues.com/uflrus/blog/news/36941/test",
  "name": "Daddyleagues",
  "avatar_url": "http://daddyleagues.com/img/daddy_league_logo.png"
}


Request Details
POST	https://webhook.site/ddab3b7d-75e9-4d23-ac40-3dee71f0d416
Host	2600:3c02::f03c:91ff:feae:2057 whois
Date	Apr 30, 2020 4:46 PM (a few seconds ago)
Size	194 bytes
ID	6cc8c48d-477b-4aea-8842-ea7012c5a5dd
Headers
connection	close
content-length	194
content-type	application/json
accept
    host	webhook.site
    Query strings
    (empty)
    Form values
    (empty)
    Format JSON   Word-Wrap  Copy
    Raw Content

    {
        "content": " A new article has been posted http://daddyleagues.com/uflrus/blog/news/36941/test",
        "name": "Daddyleagues",
        "avatar_url": "http://daddyleagues.com/img/daddy_league_logo.png"
    }
     */
