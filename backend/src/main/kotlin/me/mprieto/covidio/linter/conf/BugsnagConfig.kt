package me.mprieto.covidio.linter.conf

import com.bugsnag.Bugsnag
import com.bugsnag.BugsnagSpringConfiguration
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@Import(BugsnagSpringConfiguration::class)
@ConditionalOnProperty("bugsnag.key")
class BugsnagConfig {

    @Value("\${bugsnag.key}")
    private lateinit var key: String

    @Bean
    fun bugsnag(): Bugsnag {
        return Bugsnag(key)
    }
}
