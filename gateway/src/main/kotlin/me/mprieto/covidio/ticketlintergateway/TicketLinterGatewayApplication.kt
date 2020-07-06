package me.mprieto.covidio.ticketlintergateway

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.netflix.zuul.EnableZuulProxy

@SpringBootApplication
@EnableZuulProxy
class TicketLinterGatewayApplication

fun main(args: Array<String>) {
	runApplication<TicketLinterGatewayApplication>(*args)
}
