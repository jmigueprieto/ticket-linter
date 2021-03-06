package me.mprieto.covidio.linter.services.atlassian

import com.atlassian.connect.spring.AtlassianHostRestClients
import me.mprieto.covidio.linter.exceptions.RestClientException
import me.mprieto.covidio.linter.services.atlassian.Jira.*
import me.mprieto.covidio.linter.services.pagination.Page
import org.slf4j.Logger
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.RequestEntity
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClientResponseException
import org.springframework.web.client.exchange
import java.lang.IllegalArgumentException
import java.net.URI

@Service
class JiraCloudService(private val log: Logger, private val restClient: AtlassianHostRestClients) {

    companion object {
        // https://developer.atlassian.com/cloud/jira/platform/rest/v3/api-group-issues/#api-rest-api-3-issue-issueidorkey-get
        private const val ISSUE_PATH = "/rest/api/3/issue/"

        // https://developer.atlassian.com/cloud/jira/platform/rest/v3/#api-rest-api-3-project-get
        private const val PATH_PROJECT = "/rest/api/3/project"

        // https://developer.atlassian.com/cloud/jira/platform/rest/v3/#api-rest-api-3-search-post
        private const val PATH_SEARCH = "/rest/api/3/search"

        private val PROJECT_KEY_REGEX = "[a-zA-Z0-9]{2,10}".toRegex()

        const val DEFAULT_PAGE_SIZE = 50

    }

    /**
     * Returns a list of all Jira Projects. If Jira responds with an error it throws a RestClientException
     */
    @Throws(RestClientException::class)
    fun projects() = getOrThrowException {
        val request = RequestEntity<Any>(HttpMethod.GET, URI(PATH_PROJECT))
        val response: ResponseEntity<List<Project>> = restClient.authenticatedAsAddon().exchange(request)

        if (response.statusCode != HttpStatus.OK) {
            log.error("GET projects response was '{}'. Was Expecting 200.", response.statusCode)
            throw RestClientException("Error while getting projects")
        }

        response.body!!
    }


    /**
     * Returns a list of ALL unresolved issues with issuetype equal to "Story".
     *
     * @param projectKey the Key of the Jira Project in which to search for tickets
     */
    fun issues(projectKey: String): List<Issue> {
        data class PageCounter(var startAt: Int = 0) {
            fun next(total: Int): Boolean {
                startAt += DEFAULT_PAGE_SIZE
                // if the page was full, request the next page
                return startAt < total
            }
        }

        val counter = PageCounter()
        val issues = mutableListOf<Issue>()
        do {
            log.trace("Requesting issues from '{}', page size '{}'", counter.startAt, DEFAULT_PAGE_SIZE)
            val page = issues(projectKey, counter.startAt, DEFAULT_PAGE_SIZE)
            log.trace("Found '{}' issues from start at '{}' with page size '{}'",
                    page.data.size, counter.startAt, DEFAULT_PAGE_SIZE)
            issues.addAll(page.data)
        } while (counter.next(page.total))

        return issues
    }

    /**
     * Returns a paginated list of unresolved issues with issuetype equal to "Story".
     *
     * @param projectKey the Key of the Jira Project in which to search for tickets
     */
    @Throws(RestClientException::class)
    fun issues(projectKey: String, startAt: Int, maxResults: Int) = getOrThrowException {
        if (!projectKey.matches(PROJECT_KEY_REGEX)) {
            throw IllegalArgumentException("Invalid key '$projectKey'")
        }

        val requestBody = IssueSearchRequestBody(
                startAt = startAt,
                maxResults = maxResults,
                fields = listOf("issuetype", "status", "summary", "description"),
                jql = "project = $projectKey AND issuetype = Story AND resolution = Unresolved ORDER BY key DESC")

        val request = RequestEntity(requestBody, HttpMethod.POST, URI(PATH_SEARCH))
        val response: ResponseEntity<IssueSearch> = restClient.authenticatedAsAddon().exchange(request)
        if (response.statusCode != HttpStatus.OK) {
            log.error("SEARCH issues response was '{}'. Was Expecting 200.", response.statusCode)
            throw RestClientException("Error while searching issues")
        }

        val search = response.body!!
        // total is "The number of results on the page." according to the doc
        // but on the actual response it seems to be the number of issues in the search
        Page(data = search.issues, total = search.total)
    }

    @Throws(RestClientException::class)
    fun issue(issueKey: String) = getOrThrowException {
        val request = RequestEntity<Any>(HttpMethod.GET, URI("$ISSUE_PATH/$issueKey"))
        val response: ResponseEntity<Issue> = restClient.authenticatedAsAddon().exchange(request)

        if (response.statusCode != HttpStatus.OK) {
            log.error("GET issue '{}' response was '{}'. Was Expecting 200.", issueKey, response.statusCode)
            throw RestClientException("Error while getting issue '$issueKey'")
        }

        response.body!!
    }

    private fun <T> getOrThrowException(lambda: () -> T): T {
        try {
            return lambda()
        } catch (e: RestClientResponseException) {
            log.error("Error while making a request to Jira", e)
            throw RestClientException("Error while making a request to Jira. Response status code: '${e.rawStatusCode}'", e)
        }
    }

}

private data class IssueSearchRequestBody(val fields: List<String>, val startAt: Int, val maxResults: Int, val jql: String)
