package kt.sandbox.processor

import org.springframework.beans.factory.annotation.Autowired

class ExecuteAction(
        @Autowired
        private val daddyProcessor: DaddyProcessor) : Runnable {


    override fun run() {
        TODO("Not yet implemented")
    }
}