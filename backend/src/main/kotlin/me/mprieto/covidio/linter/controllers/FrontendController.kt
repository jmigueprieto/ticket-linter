package me.mprieto.covidio.linter.controllers

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class FrontendController {

    @GetMapping(value = ["/projects", "/projects/"])
    fun projects() = "redirect:/projects/index.html"

    @GetMapping(value = ["/issues", "/issues/"])
    fun issues() = "redirect:/issues/index.html"

}
