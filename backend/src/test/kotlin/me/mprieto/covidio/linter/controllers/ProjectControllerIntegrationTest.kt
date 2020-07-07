package me.mprieto.covidio.linter.controllers

import me.mprieto.covidio.linter.utils.whenever
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.get
import me.mprieto.covidio.linter.services.atlassian.Jira.*
import org.mockito.Mockito.*

class ProjectControllerIntegrationTest : RestControllerTest() {

    @Test
    @DisplayName("when getting projects with no token expect a 401")
    fun returns401() {
        mockMvc.get("/linter/api/projects") {
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isUnauthorized }
        }

        verify(jiraService, never()).projects()

    }

    @Test
    @DisplayName("when getting evaluation with valid token expect a 200 - less than page size")
    fun returns200() {
        val projects = generateListOfProjects()
        whenever(jiraService.projects()).thenReturn(projects)

        val expectedJson = mapper.writeValueAsString(listOf(
                mapOf("key" to "COV",
                        "name" to "Covidio",
                        "status" to "NONE",
                        "lastUpdate" to "N/A",
                        "total" to "N/A",
                        "violations" to "N/A"),
                mapOf("key" to "EL",
                        "name" to "Elvis",
                        "status" to "NONE",
                        "lastUpdate" to "N/A",
                        "total" to "N/A",
                        "violations" to "N/A")))

        mockMvc.get("/linter/api/projects?jwt=${token}") {
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk }
            content { contentType(MediaType.APPLICATION_JSON) }
            content { json(expectedJson) }
        }

        verify(jiraService, times(1)).projects()
    }

    private fun generateListOfProjects() = listOf(
            Project(id = "10000",
                    key = "COV",
                    name = "Covidio",
                    self = "https://covidio.atlassian.net/rest/api/3/project/10000",
                    projectTypeKey = "software",
                    style = "classic",
                    avatarUrls = emptyMap()),
            Project(id = "10001",
                    key = "EL",
                    name = "Elvis",
                    self = "https://covidio.atlassian.net/rest/api/3/project/10001",
                    projectTypeKey = "software",
                    style = "classic",
                    avatarUrls = emptyMap()))


}