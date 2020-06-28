package me.mprieto.covidio.linter

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication
@EnableJpaRepositories
class TicketLinterApplication

fun main(args: Array<String>) {
    runApplication<TicketLinterApplication>(*args)
}
