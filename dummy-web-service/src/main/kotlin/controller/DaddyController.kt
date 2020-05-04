package kt.sandbox.controller

import com.codeborne.selenide.Selenide.*
import com.pengrad.telegrambot.request.SendAnimation
import kt.sandbox.utils.Bot
import org.openqa.selenium.By
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.*
import kt.sandbox.processor.DaddyProcessor
import org.springframework.beans.factory.annotation.Autowired
import java.lang.Exception

@RestController
class DaddyController(
    @Autowired
    private val daddyProcessor: DaddyProcessor
) {

    val logger = LoggerFactory.getLogger(javaClass)

    @PostMapping("/daddyleague/{chat_id}")
    fun postWebHook(@RequestBody body: Map<String, String>, @PathVariable chat_id: Long) {
        try {
            daddyProcessor.parseMessage(chat_id, body)
        } catch (e: Exception) {
            logger.error(e.message)
            logger.info("Second try")
            daddyProcessor.parseMessage(chat_id, body)
        }
    }

    @GetMapping("/daddyleague/rage/{chat_id}")
    fun rage(@PathVariable chat_id: Long) {
        daddyProcessor.rage(chat_id)
    }

}