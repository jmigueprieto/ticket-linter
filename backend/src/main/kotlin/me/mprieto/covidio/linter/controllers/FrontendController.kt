package me.mprieto.covidio.linter.controllers

import me.mprieto.covidio.linter.services.atlassian.JiraCloudService
import me.mprieto.covidio.linter.services.validators.ValidatorService
import org.slf4j.Logger
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.ModelAndView

//TODO tests
@Controller
class FrontendController(private val log: Logger,
                         private val jiraService: JiraCloudService,
                         private val validatorService: ValidatorService) {

    @GetMapping(value = ["/projects", "/projects/"])
    fun projects() = "redirect:/projects/index.html"

    @GetMapping(value = ["/issues", "/issues/"])
    fun issues() = "redirect:/issues/index.html"

    @GetMapping("/issue")
    fun issue(@RequestParam("issue_key") issueKey: String, model: Model): String {
        val issue = jiraService.issue(issueKey)
        log.debug("issue={}", issue)
        model.addAttribute("issue", issue)
        if (issue.isStory) {
            val validation = validatorService.validate(issue.descriptionText)
            model.addAttribute("validation", validation)
        }
        return "issue_web_panel"
    }
}
