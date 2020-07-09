package me.mprieto.covidio.linter.utils

import me.mprieto.covidio.linter.services.atlassian.ADFNode
import me.mprieto.covidio.linter.services.atlassian.Jira
import org.mockito.Mockito
import org.springframework.core.ParameterizedTypeReference
import org.springframework.core.io.ClassPathResource

fun <T> whenever(x: T) = Mockito.`when`(x)!!

fun getResourceAsString(path: String) = ClassPathResource(path).url.readText()

inline fun <reified T : Any> mock() = Mockito.mock(T::class.java)!!

// https://stackoverflow.com/questions/39679180/kotlin-call-java-method-with-classt-argument
inline fun <reified T : Any> typeRef(): ParameterizedTypeReference<T> = object : ParameterizedTypeReference<T>() {}

fun generateListOfIssues(start: Int, end: Int) = (start..end).map {
    val storyType = Jira.IssueType("10001", "Story", "Functionality or a feature expressed as a user goal.")
    val issueFields = Jira.IssueFields(storyType, "Aloha From Hawaii! - $it", ADFNode("doc", emptyList(), ""))
    val id = 10000 + it
    Jira.Issue(id = "$id", key = "COV-$it", self = "https://covidio.atlassian.net/rest/api/3/issue/$id", fields = issueFields)
}
