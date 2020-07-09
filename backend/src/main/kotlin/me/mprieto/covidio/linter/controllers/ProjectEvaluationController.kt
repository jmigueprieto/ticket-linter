package me.mprieto.covidio.linter.controllers

import com.atlassian.connect.spring.AtlassianHostUser
import me.mprieto.covidio.linter.services.atlassian.Jira.*
import me.mprieto.covidio.linter.services.atlassian.JiraCloudService
import me.mprieto.covidio.linter.services.validators.ValidationResult
import me.mprieto.covidio.linter.services.validators.ValidatorService
import org.slf4j.Logger
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RestController
@CrossOrigin
class ProjectEvaluationController(private val log: Logger,
                                  private val jiraService: JiraCloudService,
                                  private val validatorService: ValidatorService) {

    @GetMapping(value = ["/linter/api/projects/{key}/evaluation"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun evaluation(@AuthenticationPrincipal user: AtlassianHostUser,
                   @PathVariable("key") projectKey: String): ResponseEntity<Any> {
        val host = user.host.baseUrl
        log.debug("Evaluating project: '{}' of host '{}'", projectKey, host)

        val data = jiraService.issues(projectKey)
        val total = data.size

        val stories = data.map {
            val key = it.key
            val summary = it.summary
            val description = it.descriptionText
            val validationResult = validatorService.validate(description)
            val url = "$host/browse/$key"
            Story(key, summary, description, url, Validation(validationResult))
        }
        val violations = stories.count { !it.validation.isValid }
        val timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))

        val responseBody = Evaluation(total, timestamp, violations, stories)
        log.debug("Returning total: '{}', timestamp: '{}', violations: '{}'", total, data, violations)
        log.trace("Response body: {}", responseBody)
        return ResponseEntity.ok(responseBody)
    }

}

private data class Validation(val isValid: Boolean, val messages: List<String>) {
    constructor(res: ValidationResult) : this(res.isValid, res.messages)
}

private data class Story(val key: String,
                         val summary: String,
                         val description: String,
                         val url: String,
                         val validation: Validation)

private data class Evaluation(val total: Int,
                              val timestamp: String,
                              val violations: Int,
                              val stories: List<Story>)
