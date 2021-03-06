package me.mprieto.covidio.linter.controllers

import com.atlassian.connect.spring.AtlassianHostUser
import me.mprieto.covidio.linter.services.atlassian.JiraCloudService
import org.slf4j.Logger
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@CrossOrigin
class ProjectController(private val log: Logger,
                        private val jiraService: JiraCloudService) {

    @GetMapping("/linter/api/projects")
    fun projects(@AuthenticationPrincipal user: AtlassianHostUser): ResponseEntity<List<Any>> {
        log.debug("Listing projects for '{}'", user.host.baseUrl)
        val projects = jiraService.projects()
        val responseBody = projects.map {
            mapOf("key" to it.key,
                    "status" to "NONE",
                    "name" to it.name,
                    "lastUpdate" to "N/A",
                    "total" to "N/A",
                    "violations" to "N/A")
        }

        log.trace("Response body: {}", responseBody)
        return ResponseEntity.ok(responseBody)
    }

}