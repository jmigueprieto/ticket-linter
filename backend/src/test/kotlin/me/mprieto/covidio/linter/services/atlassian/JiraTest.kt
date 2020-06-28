package me.mprieto.covidio.linter.services.atlassian

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import me.mprieto.covidio.linter.utils.getResourceAsString
import org.junit.jupiter.api.Test
import me.mprieto.covidio.linter.services.atlassian.Jira.Issue
import me.mprieto.covidio.linter.services.atlassian.Jira.IssueFields
import me.mprieto.covidio.linter.services.atlassian.Jira.IssueType
import org.junit.jupiter.api.Assertions.*

class JiraTest {

    private val mapper = ObjectMapper().registerModule(KotlinModule())
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    private val storyType = IssueType("10001", "Story", "Functionality or a feature expressed as a user goal.")

    @Test
    fun `when getting summary from issue expect value set in IssueFields`() {
        val issueFields = IssueFields(storyType, "Aloha From Hawaii!", ADFNode("doc", emptyList(), ""))
        val issue = Issue(
                id = "10000",
                key = "COV-1",
                self = "https://covidio.atlassian.net/rest/api/3/issue/10000",
                fields = issueFields)
        assertEquals("Aloha From Hawaii!", issue.summary)
    }

    @Test
    fun `when getting the description text from an issue with hello-world sample expect "hello world"`() {
        val description: ADFNode = mapper.readValue(getResourceAsString("/samples/adf/hello-world.json"))
        val issueFields = IssueFields(storyType, "Greet the world", description)
        val issue = Issue(
                id = "10000",
                key = "COV-1",
                self = "https://covidio.atlassian.net/rest/api/3/issue/10000",
                fields = issueFields)
        assertEquals("Greet the world", issue.summary)
        assertEquals("Hello world\n", issue.descriptionText)
    }

    @Test
    fun `when parsing a hello-world expect ADFNode to be equal to expected"`() {
        val helloWorld: ADFNode = mapper.readValue(getResourceAsString("/samples/adf/hello-world.json"))
        val expected = ADFNode(type = "doc",
                text = null,
                content =
                listOf(ADFNode(type = "paragraph",
                        text = null,
                        content =
                        listOf(ADFNode(type = "text", content = null, text = "Hello "),
                                ADFNode(type = "text", content = null, text = "world"))))
        )
        assertEquals(expected, helloWorld)
    }

    @Test
    fun `when parsing issue cov-1 expect all fields to be set correctly"`() {
        val issue: Issue = mapper.readValue(getResourceAsString("/samples/issues/cov-1.json"))
        assertEquals("10000", issue.id)
        assertEquals("https://covidio.atlassian.net/rest/api/3/issue/10000", issue.self)
        assertEquals("COV-1", issue.key)
        assertEquals("Dashboard", issue.summary)
        val expectedText = "US\n" +
                "As a user I want to see a dashboard so that I can get quick info about ticket quality in my projects\n" +
                "AC\n" +
                "User sees a dashboard\n" +
                "Dashboard shows the number of analyzed tickets, flagged tickets, etc.\n" +
                "User sees those numbers broken down by project.\n" +
                "User has the options of viewing more details.\n" +
                "User has the option to trigger a full project analysis (all tickets are evaluated).\n"
        assertEquals(expectedText, issue.descriptionText)
    }
}