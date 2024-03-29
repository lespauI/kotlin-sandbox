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
import org.springframework.scheduling.TaskScheduler
import java.lang.Exception

@RestController
class DaddyController(
        @Autowired
        private val daddyProcessor: DaddyProcessor
) {

    var failedList: ArrayList<Map<Long, Map<String, String>>> = arrayListOf()
    var executionList: ArrayList<Map<Long, Map<String, String>>> = arrayListOf()

    val logger = LoggerFactory.getLogger(javaClass)

    @PostMapping("/daddyleague/{chat_id}")
    fun postWebHook(@RequestBody body: Map<String, String>, @PathVariable chat_id: Long): ResponseEntity<String> {
        val map = hashMapOf<Long, Map<String, String>>()
        map[chat_id] = body
        executionList.add(map)
        return try {
            daddyProcessor.parseMessage(chat_id, body)
            executionList.remove(map)
            ResponseEntity("\nIs Ok!", HttpStatus.OK)
        } catch (e: Exception) {
            closeWebDriver()
            logger.error("Exception! $e.message")
            failedList.add(map)
            ResponseEntity("\nError qwer $e.message", HttpStatus.NO_CONTENT)
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

    @GetMapping("/daddyleague/get/{type}")
    fun get(@PathVariable(required = false) type: String): ResponseEntity<String> {
        try {
            if (type.equals("failed")) {
                return ResponseEntity("Size = ${failedList.size}\n$failedList", HttpStatus.OK)
            } else if (type.equals("msg")) {
                return ResponseEntity("Size = ${daddyProcessor.getMessagesList().size}\n${daddyProcessor.getMessagesList()}", HttpStatus.OK)
            }
            else
                return ResponseEntity("Size = ${executionList.size}\n$executionList", HttpStatus.OK)
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