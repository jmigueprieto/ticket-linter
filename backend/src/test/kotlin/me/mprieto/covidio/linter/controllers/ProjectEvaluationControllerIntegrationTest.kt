package me.mprieto.covidio.linter.controllers

import me.mprieto.covidio.linter.controllers.ProjectEvaluationController.Companion.PAGE_SIZE
import me.mprieto.covidio.linter.services.atlassian.ADFNode
import me.mprieto.covidio.linter.services.atlassian.Jira.*
import me.mprieto.covidio.linter.services.pagination.Page
import me.mprieto.covidio.linter.services.validators.ValidationResult
import me.mprieto.covidio.linter.services.validators.ValidatorService
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
    @DisplayName("when getting evaluation with valid token expect a 200 - less than page size")
    fun returns200() {
        val issues = generateListOfIssues(1, PAGE_SIZE - 1)
        whenever(jiraService.issues("COV", 0, PAGE_SIZE))
                .thenReturn(Page(data = issues, total = issues.size))
        whenever(validatorService.validate(anyString())).thenReturn(ValidationResult(false, "Empty Story"))

        mockMvc.get("/linter/api/projects/COV/evaluation?jwt=${token}") {
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk }
            content { contentType(MediaType.APPLICATION_JSON) }
            content { json(mapper.writeValueAsString(mapOf("total" to issues.size, "violations" to issues.size))) }
        }

        verify(jiraService, times(1)).issues("COV", 0, PAGE_SIZE)
        verify(validatorService, times(issues.size)).validate(anyString())
    }

    @Test
    @DisplayName("when getting evaluation with valid token expect a 200 - same as page size")
    fun returns200_with_pagination_1() {
        whenever(jiraService.issues("COV", 0, PAGE_SIZE))
                .thenReturn(Page(data = generateListOfIssues(1, PAGE_SIZE), total = PAGE_SIZE))
        whenever(jiraService.issues("COV", PAGE_SIZE, PAGE_SIZE))
                .thenReturn(Page(data = emptyList(), total = 0))
        whenever(validatorService.validate(anyString())).thenReturn(ValidationResult(true))

        mockMvc.get("/linter/api/projects/COV/evaluation?jwt=${token}") {
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk }
            content { contentType(MediaType.APPLICATION_JSON) }
            content { json(mapper.writeValueAsString(mapOf("total" to PAGE_SIZE, "violations" to 0))) }
        }

        verify(jiraService, times(1)).issues("COV", 0, PAGE_SIZE)
        verify(jiraService, times(1)).issues(anyString(), anyInt(), anyInt())
        verify(validatorService, times(PAGE_SIZE)).validate(anyString())
    }

    @Test
    @DisplayName("when getting evaluation with valid token expect a 200 - greater than page size")
    fun returns200_with_pagination_2() {
        val gt = PAGE_SIZE - 5
        whenever(jiraService.issues("COV", 0, PAGE_SIZE))
                .thenReturn(Page(data = generateListOfIssues(1, PAGE_SIZE), total = PAGE_SIZE + gt))
        whenever(jiraService.issues("COV", PAGE_SIZE, PAGE_SIZE))
                .thenReturn(Page(data = generateListOfIssues(PAGE_SIZE + 1, PAGE_SIZE + gt), total = PAGE_SIZE + gt))
        whenever(validatorService.validate(anyString())).thenReturn(ValidationResult(false, "Empty Story"))

        mockMvc.get("/linter/api/projects/COV/evaluation?jwt=${token}") {
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk }
            content { contentType(MediaType.APPLICATION_JSON) }
            content { json(mapper.writeValueAsString(mapOf("total" to PAGE_SIZE + gt, "violations" to PAGE_SIZE + gt))) }
        }

        verify(jiraService, times(1)).issues("COV", 0, PAGE_SIZE)
        verify(jiraService, times(1)).issues("COV", PAGE_SIZE, PAGE_SIZE)
        verify(validatorService, times(PAGE_SIZE + gt)).validate(anyString())
    }

    private fun generateListOfIssues(start: Int, end: Int) = (start..end).map {
        val storyType = IssueType("10001", "Story", "Functionality or a feature expressed as a user goal.")
        val issueFields = IssueFields(storyType, "Aloha From Hawaii! - $it", ADFNode("doc", emptyList(), ""))
        val id = 10000 + it
        Issue(id = "$id", key = "COV-$it", self = "https://covidio.atlassian.net/rest/api/3/issue/$id", fields = issueFields)
    }

}
