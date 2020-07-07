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
        // https://developer.atlassian.com/cloud/jira/platform/rest/v3/#api-rest-api-3-project-get
        private const val PATH_PROJECT = "/rest/api/3/project"

        // https://developer.atlassian.com/cloud/jira/platform/rest/v3/#api-rest-api-3-search-post
        private const val PATH_SEARCH = "/rest/api/3/search"

        private val PROJECT_KEY_REGEX = "[a-zA-Z0-9]{2,10}".toRegex()
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
     * Returns a paginated list of unresolved issues with issuetype equal to "Story".
     *
     * @param projectKey the Key of the Jira Project in which to search for tickets
     */
    @Throws(RestClientException::class)
    fun issues(projectKey: String, startAt: Int, maxResults: Int) = getOrThrowException {
        if (!projectKey.matches(PROJECT_KEY_REGEX)) {
            throw IllegalArgumentException("Invalid key '$projectKey'")
        }

        data class RequestBody(val fields: List<String>, val startAt: Int, val maxResults: Int, val jql: String)

        val requestBody = RequestBody(
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
        // search.total is the number of results in the page
        Page(data = search.issues, total = search.total)
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


