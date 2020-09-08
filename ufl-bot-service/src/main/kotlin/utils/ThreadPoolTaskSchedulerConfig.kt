package kt.sandbox.utils

import kt.sandbox.UflBotServiceApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler


@Configuration
@ComponentScan(basePackages = ["com.baeldung.taskscheduler"], basePackageClasses = [UflBotServiceApplication::class])
open class ThreadPoolTaskSchedulerConfig {
    @Bean
    open fun threadPoolTaskScheduler(): ThreadPoolTaskScheduler {
        val threadPoolTaskScheduler = ThreadPoolTaskScheduler()
        threadPoolTaskScheduler.poolSize = 5
        threadPoolTaskScheduler.threadNamePrefix = "ThreadPoolTaskScheduler"
        return threadPoolTaskScheduler
    }
}