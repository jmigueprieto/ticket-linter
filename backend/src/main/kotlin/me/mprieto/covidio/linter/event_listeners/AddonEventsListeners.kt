package me.mprieto.covidio.linter.event_listeners

import com.atlassian.connect.spring.AddonInstalledEvent
import com.atlassian.connect.spring.AddonUninstalledEvent
import org.slf4j.Logger
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class AddonEventsListeners(private val log: Logger) {

    @EventListener
    fun handleAddonInstalled(event: AddonInstalledEvent) {
        log.info("Addon installed in host '{}'", event.host.baseUrl)
    }

    @EventListener
    fun handleAddonUninstalled(event: AddonUninstalledEvent) {
        log.info("Addon uninstalled in host '{}'", event.host.baseUrl)
    }
}
