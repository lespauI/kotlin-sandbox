package kt.sandbox.controller

import com.codeborne.selenide.Selenide
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

    var failedList: ArrayList<Map<Long, Map<String, String>>> = arrayListOf()

    val logger = LoggerFactory.getLogger(javaClass)

    @PostMapping("/daddyleague/{chat_id}")
    fun postWebHook(@RequestBody body: Map<String, String>, @PathVariable chat_id: Long): ResponseEntity<String> {
        try {
            daddyProcessor.parseMessage(chat_id, body)
            return ResponseEntity("\nIs Ok!", HttpStatus.OK)
        } catch (e: Exception) {
            closeWebDriver()
            logger.error("Exception! $e.message")
            //try {
            //    Thread.sleep(5000)
            //    daddyProcessor.parseMessage(chat_id, body)
            //    return ResponseEntity("\nIs Ok! But from 2nd time", HttpStatus.OK)
            //} catch (e: Exception) {
            daddyProcessor.sendDebug("chat_id = $chat_id for message = $body", e)
            val map = hashMapOf<Long, Map<String, String>>()
            map[chat_id] = body
            failedList.add(map)
            return ResponseEntity("\nError qwer $e.message", HttpStatus.NO_CONTENT)
            //}
        }
    }

    @GetMapping("/daddyleague/push")
    fun push(): ResponseEntity<String> {
        //TODO think about it
        if (failedList.size == 0)
            return ResponseEntity("Empty list", HttpStatus.NO_CONTENT)
        else {
            val tmp = failedList

            while (failedList.size > 0) {
                for (e in failedList) {
                    try {
                        e.get(e.keys.first())?.let {
                            postWebHook(it, e.keys.first())
                        }
                        failedList.remove(e)
                    } catch (e: Exception) {
                    }
                }
            }
            return ResponseEntity("Refreshed for list\n$tmp", HttpStatus.OK)
        }
    }

    @GetMapping("/daddyleague/get")
    fun get(): ResponseEntity<String> {
        try {
            return ResponseEntity("Size = ${failedList.size}\n$failedList", HttpStatus.OK)
        } catch (e: Exception) {
            return ResponseEntity("Error $e.message\n", HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @GetMapping("/daddyleague/rage/{chat_id}")
    fun rage(@PathVariable chat_id: Long): ResponseEntity<String> {
        try {
            daddyProcessor.rage(chat_id)
            return ResponseEntity("Is Ok!\n", HttpStatus.OK)
        } catch (e: Exception) {
            return ResponseEntity("Error $e.message\n", HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @GetMapping("/daddyleague/schedules/{chat_id}")
    fun schedules(@PathVariable chat_id: Long): ResponseEntity<String> {
        try {
            daddyProcessor.getSchedules(chat_id, "")
            return ResponseEntity("Is Ok!\n", HttpStatus.OK)
        } catch (e: Exception) {
            return ResponseEntity("Error $e.message\n", HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @GetMapping("/daddyleague/weekgame/{chat_id}")
    fun weekgame(@PathVariable chat_id: Long): ResponseEntity<String> {
        try {
            daddyProcessor.getGameOfTheWeek(chat_id)
            return ResponseEntity("Is Ok!\n", HttpStatus.OK)
        } catch (e: Exception) {
            return ResponseEntity("Error $e.message\n", HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }
}