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
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.lang.Exception

@RestController
class DaddyController(
    @Autowired
    private val daddyProcessor: DaddyProcessor
) {

    val logger = LoggerFactory.getLogger(javaClass)

    @PostMapping("/daddyleague/{chat_id}")
    fun postWebHook(@RequestBody body: Map<String, String>, @PathVariable chat_id: Long): ResponseEntity<String> {
        try {
            daddyProcessor.parseMessage(chat_id, body)
            return ResponseEntity("Is Ok!", HttpStatus.OK)

        } catch (e: Exception) {
            logger.error("Exception! $e.message")
            return ResponseEntity(":(", HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @GetMapping("/daddyleague/rage/{chat_id}")
    fun rage(@PathVariable chat_id: Long): ResponseEntity<String> {
        try {
            daddyProcessor.rage(chat_id)
            return ResponseEntity("Is Ok!", HttpStatus.OK)
        } catch (e: Exception) {
            return ResponseEntity(":(", HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @GetMapping("/daddyleague/schedules/{chat_id}")
    fun schedules(@PathVariable chat_id: Long): ResponseEntity<String> {
        try {
            daddyProcessor.getSchedules(chat_id, "")
            return ResponseEntity("Is Ok!", HttpStatus.OK)
        } catch (e: Exception) {
            return ResponseEntity(":(", HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @GetMapping("/daddyleague/weekgame/{chat_id}")
    fun weekgame(@PathVariable chat_id: Long): ResponseEntity<String> {
        try {
            daddyProcessor.getGameOfTheWeek(chat_id)
            return ResponseEntity("Is Ok!", HttpStatus.OK)
        } catch (e: Exception) {
            return ResponseEntity(":(", HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

}