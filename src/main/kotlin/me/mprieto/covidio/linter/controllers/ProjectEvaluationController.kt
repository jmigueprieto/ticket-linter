package me.mprieto.covidio.linter.controllers

import com.atlassian.connect.spring.AtlassianHostUser
import me.mprieto.covidio.linter.services.atlassian.JiraCloudService
import me.mprieto.covidio.linter.services.validators.ValidationResult
import me.mprieto.covidio.linter.services.validators.ValidatorService
import org.slf4j.Logger
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

//TODO Integration Tests
@RestController
@CrossOrigin(origins = ["https://covidio.ngrok.io"])
class ProjectEvaluationController(private val log: Logger,
                                  private val atlassianService: JiraCloudService,
                                  private val userStoryService: ValidatorService) {

    @GetMapping("/linter/api/projects/{key}/evaluation")
    fun evaluation(@AuthenticationPrincipal user: AtlassianHostUser,
                   @PathVariable("key") projectKey: String): ResponseEntity<Any> {
        val host = user.host.baseUrl
        log.debug("Listing issues for '{}'", host)
        //FIXME Needs pagination
        val page = atlassianService.issues(projectKey)
        log.debug("total '{}', found issues: '{}'", page.total, page.data)

        val stories = page.data.map {
            val key = it.key
            val summary = it.fields.summary
            val description = it.fields.descriptionText
            val validationResult = userStoryService.validate(description)
            val url = "$host/browse/$key"
            Story(key, summary, description, url, Validation(validationResult))
        }
        val violations = stories.count { !it.validation.isValid }
        val timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))

        val responseBody = Evaluation(page.total, timestamp, violations, stories)

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
