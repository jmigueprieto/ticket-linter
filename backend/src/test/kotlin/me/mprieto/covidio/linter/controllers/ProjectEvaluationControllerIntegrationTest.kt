package me.mprieto.covidio.linter.controllers

import me.mprieto.covidio.linter.services.atlassian.ADFNode
import me.mprieto.covidio.linter.services.atlassian.Jira.*
import me.mprieto.covidio.linter.services.validators.ValidationResult
import me.mprieto.covidio.linter.services.validators.ValidatorService
import me.mprieto.covidio.linter.utils.generateListOfIssues
import me.mprieto.covidio.linter.utils.whenever
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.get


class ProjectEvaluationControllerIntegrationTest : RestControllerTest() {

    @MockBean
    private lateinit var validatorService: ValidatorService

    @Test
    @DisplayName("when getting evaluation with no token expect a 401")
    fun returns401() {
        mockMvc.get("/linter/api/projects/COV/evaluation") {
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isUnauthorized }
        }

        verify(jiraService, never()).issues(anyString(), anyInt(), anyInt())
        verify(validatorService, never()).validate(anyString())
    }

    @Test
    @DisplayName("when getting evaluation with valid token expect a 200")
    fun returns200() {
        val issues = generateListOfIssues(1, 100)
        whenever(jiraService.issues("COV"))
                .thenReturn(issues)
        whenever(validatorService.validate(anyString())).thenReturn(ValidationResult(false, "Empty Story"))

        mockMvc.get("/linter/api/projects/COV/evaluation?jwt=${token}") {
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk }
            content { contentType(MediaType.APPLICATION_JSON) }
            content { json(mapper.writeValueAsString(mapOf("total" to issues.size, "violations" to issues.size))) }
        }

        verify(jiraService, times(1)).issues("COV")
        verify(validatorService, times(issues.size)).validate(anyString())
    }


}
