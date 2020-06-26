package me.mprieto.covidio.linter.services.atlassian

import com.atlassian.connect.spring.AtlassianHostRestClients
import me.mprieto.covidio.linter.exceptions.RestClientException
import org.slf4j.Logger
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.RequestEntity
import org.springframework.stereotype.Service
import java.net.URI


//FIXME externalize API paths
@Service
class JiraCloudService(private val log: Logger,
                       private val restClient: AtlassianHostRestClients) {

    // https://stackoverflow.com/questions/39679180/kotlin-call-java-method-with-classt-argument
    private inline fun <reified T : Any> typeRef(): ParameterizedTypeReference<T> = object : ParameterizedTypeReference<T>() {}

    fun projects(): List<Jira.Project> {
        // https://developer.atlassian.com/cloud/jira/platform/rest/v3/#api-rest-api-3-project-get
        val response = restClient.authenticatedAsAddon().exchange(RequestEntity<Any>(HttpMethod.GET, URI("/rest/api/3/project")),
                typeRef<List<Jira.Project>>())
        if (response.statusCode != HttpStatus.OK) {
            log.error("GET projects response was '{}'", response.statusCode)
            throw RestClientException("Error while getting projects")
        }

        return response.body!!
    }

    //FIXME parameterize jql
    //FIXME sanitize projectKey to avoid injection or use project id which seems to be an integer...?
    fun issues(projectKey: String, startAt: Int = 0, maxResults: Int = 50): Jira.Page<List<Jira.Issue>> {
        data class RequestBody(val fields: List<String>, val startAt: Int, val maxResults: Int, val jql: String)

        val requestBody = RequestBody(
                startAt = startAt,
                maxResults = maxResults,
                fields = listOf("issuetype", "status", "summary", "description"),
                jql = "project = $projectKey AND issuetype = Story AND resolution = Unresolved ORDER BY key DESC")

        // https://developer.atlassian.com/cloud/jira/platform/rest/v3/#api-rest-api-3-search-post
        val request = RequestEntity(requestBody, HttpMethod.POST, URI("/rest/api/3/search"))
        val response = restClient.authenticatedAsAddon().exchange(request, typeRef<Jira.IssueSearch>())
        if (response.statusCode != HttpStatus.OK) {
            log.error("SEARCH issues response was '{}'", response.statusCode)
            throw RestClientException("Error while searching issues")
        }

        val search = response.body!!
        return Jira.Page(data = search.issues, total = search.total)
    }

}


