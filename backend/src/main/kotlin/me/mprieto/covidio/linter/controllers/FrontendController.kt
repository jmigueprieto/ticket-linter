package me.mprieto.covidio.linter.controllers

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.ModelAndView

@Controller
class FrontendController {

    @GetMapping(value = ["/projects", "/projects/"])
    fun projects() = "redirect:/projects/index.html"

    @GetMapping(value = ["/issues", "/issues/"])
    fun issues() = "redirect:/issues/index.html"

    @GetMapping("/issue")
    fun issue(@RequestParam("issue_key") issueKey: String): ModelAndView {
        return ModelAndView("issue_web_panel", mapOf("issueKey" to issueKey))
    }
}
