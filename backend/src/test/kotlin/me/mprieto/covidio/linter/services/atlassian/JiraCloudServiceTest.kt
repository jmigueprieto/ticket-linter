package me.mprieto.covidio.linter.services.atlassian

import com.atlassian.connect.spring.AtlassianHostRestClients
import com.fasterxml.jackson.module.kotlin.readValue
import me.mprieto.covidio.linter.exceptions.RestClientException
import me.mprieto.covidio.linter.services.atlassian.Jira.*
import me.mprieto.covidio.linter.services.pagination.Page
import me.mprieto.covidio.linter.utils.*
import me.mprieto.covidio.linter.utils.TestUtils.Companion.MAPPER
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.eq
import org.mockito.Mockito.*
import org.slf4j.Logger
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.RequestEntity
import org.springframework.http.ResponseEntity
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate
import java.lang.IllegalArgumentException
import java.net.URI


class JiraCloudServiceTest {

    @Test
    fun `when invoking projects() if restClient successfully retrieves a list of projects expect it to be returned`() {
        val log: Logger = mock()
        val restClient: AtlassianHostRestClients = mock()
        val restTemplate: RestTemplate = mock()
        whenever(restClient.authenticatedAsAddon()).thenReturn(restTemplate)

        val service = JiraCloudService(log, restClient)
        val requestEntity = RequestEntity<Any>(HttpMethod.GET, URI("/rest/api/3/project"))
        val projects: List<Project> = MAPPER.readValue(getResourceAsString("/samples/projects/project-list.json"))

        whenever(restTemplate.exchange(requestEntity, typeRef<List<Project>>()))
                .thenReturn(ResponseEntity.ok(projects))

        val foundProjects = service.projects()
        assertFalse(foundProjects.isEmpty())
        assertEquals(projects, foundProjects)
    }

    @Test
    fun `when invoking projects() if restClient response is successful but not a 200 expect an Exception`() {
        val log: Logger = mock()
        val restClient: AtlassianHostRestClients = mock()
        val restTemplate: RestTemplate = mock()
        whenever(restClient.authenticatedAsAddon()).thenReturn(restTemplate)

        val service = JiraCloudService(log, restClient)
        val requestEntity = RequestEntity<Any>(HttpMethod.GET, URI("/rest/api/3/project"))


        whenever(restTemplate.exchange(requestEntity, typeRef<List<Project>>()))
                .thenReturn(ResponseEntity.status(HttpStatus.CONTINUE).build())

        val exception: RestClientException = assertThrows { service.projects() }
        assertEquals("Error while getting projects", exception.message)
    }

    @Test
    fun `when invoking projects() if restClient fails to retrieve a list projects expect an Exception`() {
        val log: Logger = mock()
        val restClient: AtlassianHostRestClients = mock()
        val restTemplate: RestTemplate = mock()
        whenever(restClient.authenticatedAsAddon()).thenReturn(restTemplate)

        val service = JiraCloudService(log, restClient)
        val requestEntity = RequestEntity<Any>(HttpMethod.GET, URI("/rest/api/3/project"))

        whenever(restTemplate.exchange(requestEntity, typeRef<List<Project>>()))
                .thenThrow(HttpClientErrorException(HttpStatus.UNAUTHORIZED))

        val exception: RestClientException = assertThrows { service.projects() }
        assertEquals("Error while making a request to Jira. Response status code: '401'", exception.message)
    }

    @Test
    fun `when invoking issues() if restClient successfully retrieves a list of issues expect it to be returned`() {
        val log: Logger = mock()
        val restClient: AtlassianHostRestClients = mock()
        val restTemplate: RestTemplate = mock()
        whenever(restClient.authenticatedAsAddon()).thenReturn(restTemplate)

        val service = JiraCloudService(log, restClient)

        val searchResult: IssueSearch = MAPPER.readValue(getResourceAsString("/samples/issues/search-result.json"))
        whenever(restTemplate.exchange(any(), eq(typeRef<IssueSearch>())))
                .thenReturn(ResponseEntity.ok(searchResult))

        val page = service.issues("COV", 0, 500)
        assertEquals(202, page.total)
        assertEquals(2, page.data.size)
    }

    @Test
    fun `when invoking issues() if restClient response is successful but not a 200 expect an Exception`() {
        val log: Logger = mock()
        val restClient: AtlassianHostRestClients = mock()
        val restTemplate: RestTemplate = mock()
        whenever(restClient.authenticatedAsAddon()).thenReturn(restTemplate)

        val service = JiraCloudService(log, restClient)

        whenever(restTemplate.exchange(any(), eq(typeRef<IssueSearch>())))
                .thenThrow(HttpClientErrorException(HttpStatus.UNAUTHORIZED))

        val exception: RestClientException = assertThrows { service.issues("COV", 0, 50) }
        assertEquals("Error while making a request to Jira. Response status code: '401'", exception.message)
    }

    @Test
    fun `when invoking issues() if rest fails to retrieves a list of issues expect an Exception`() {
        val log: Logger = mock()
        val restClient: AtlassianHostRestClients = mock()
        val restTemplate: RestTemplate = mock()
        whenever(restClient.authenticatedAsAddon()).thenReturn(restTemplate)

        val service = JiraCloudService(log, restClient)

        // makes no sense, right?... just in case. Defensive programming
        whenever(restTemplate.exchange(any(), eq(typeRef<IssueSearch>())))
                .thenReturn(ResponseEntity.status(HttpStatus.NO_CONTENT).build())

        val exception: RestClientException = assertThrows { service.issues("COV", 0, 50) }
        assertEquals("Error while searching issues", exception.message)
    }

    @Test
    fun `when invoking issues() if projectKey is invalid expect an Exception`() {
        val log: Logger = mock()
        val restClient: AtlassianHostRestClients = mock()
        val service = JiraCloudService(log, restClient)
        // too short
        var exception: IllegalArgumentException = assertThrows { service.issues("A", 0, 50) }
        assertEquals("Invalid key 'A'", exception.message)
        // too long
        exception = assertThrows { service.issues("THISPROJECTKEYISTOOLONG", 0, 50) }
        assertEquals("Invalid key 'THISPROJECTKEYISTOOLONG'", exception.message)
        // not alphanum
        exception = assertThrows { service.issues("'';", 0, 50) }
        assertEquals("Invalid key ''';'", exception.message)
    }

    @Test
    fun `when invoking issue() if restClient successfully retrieves an issue expect it to be returned`() {
        val log: Logger = mock()
        val restClient: AtlassianHostRestClients = mock()
        val restTemplate: RestTemplate = mock()
        whenever(restClient.authenticatedAsAddon()).thenReturn(restTemplate)

        val service = JiraCloudService(log, restClient)

        val sampleIssue: Issue = MAPPER.readValue(getResourceAsString("/samples/issues/cov-1.json"))
        whenever(restTemplate.exchange(any(), eq(typeRef<Issue>())))
                .thenReturn(ResponseEntity.ok(sampleIssue))

        val issue = service.issue("COV-1")
        assertEquals("COV-1", issue.key)
    }

    @Test
    fun `when invoking issue() if restClient fails to retrieve an issue expect an Exception`() {
        val log: Logger = mock()
        val restClient: AtlassianHostRestClients = mock()
        val restTemplate: RestTemplate = mock()
        whenever(restClient.authenticatedAsAddon()).thenReturn(restTemplate)

        val service = JiraCloudService(log, restClient)

        whenever(restTemplate.exchange(any(), eq(typeRef<Issue>())))
                .thenThrow(HttpClientErrorException(HttpStatus.UNAUTHORIZED))

        val exception: RestClientException = assertThrows { service.issue("COV-1") }
        assertEquals("Error while making a request to Jira. Response status code: '401'", exception.message)
    }

    @Test
    fun `when invoking issue() if restClient response is successful but not a 200 expect an Exception`() {
        val log: Logger = mock()
        val restClient: AtlassianHostRestClients = mock()
        val restTemplate: RestTemplate = mock()
        whenever(restClient.authenticatedAsAddon()).thenReturn(restTemplate)

        val service = JiraCloudService(log, restClient)

        whenever(restTemplate.exchange(any(), eq(typeRef<Issue>())))
                .thenReturn(ResponseEntity.status(HttpStatus.CONTINUE).build())

        val exception: RestClientException = assertThrows { service.issue("COV-1") }
        assertEquals("Error while getting issue 'COV-1'", exception.message)
    }

    @Test
    fun `when invoking issues() if results are greater than page size expect pagination`() {
        val log: Logger = mock()
        val restClient: AtlassianHostRestClients = mock()
        val restTemplate: RestTemplate = mock()
        whenever(restClient.authenticatedAsAddon()).thenReturn(restTemplate)

        val service = spy(JiraCloudService(log, restClient))

        val pageSize = JiraCloudService.DEFAULT_PAGE_SIZE
        val total = pageSize * 2 + 1

        doReturn(Page(generateListOfIssues(1, pageSize), total))
                .`when`(service)
                .issues("COV", 0, pageSize) // startAt: 0, 50

        doReturn(Page(generateListOfIssues(pageSize + 1, pageSize * 2), total))
                .`when`(service)
                .issues("COV", pageSize, pageSize) // startAt: 50, 50

        doReturn(Page(generateListOfIssues(pageSize * 2 + 1, pageSize * 2 + 1), total))
                .`when`(service)
                .issues("COV", pageSize * 2, pageSize) // startAt: 100, 50

        val issues = service.issues("COV")

        verify(service, times(1)).issues("COV", 0, pageSize)
        verify(service, times(1)).issues("COV", pageSize, pageSize)
        verify(service, times(1)).issues("COV", pageSize * 2, pageSize)
        assertEquals(total, issues.size)
    }

}
