package me.mprieto.covidio.linter.conf

import org.slf4j.*
import org.springframework.beans.factory.InjectionPoint
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.annotation.*

@Configuration
class LoggingConfiguration {

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    fun logger(injectionPoint: InjectionPoint): Logger = LoggerFactory.getLogger(
            injectionPoint.methodParameter?.containingClass // constructor
                    ?: injectionPoint.field?.declaringClass) // or field injection

}