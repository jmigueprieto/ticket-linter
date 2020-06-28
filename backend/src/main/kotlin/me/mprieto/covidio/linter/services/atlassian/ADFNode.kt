package me.mprieto.covidio.linter.services.atlassian

/**
 * Atlassian Document Format Node. Official doc:
 * https://developer.atlassian.com/cloud/jira/platform/apis/document/structure/
 */
data class ADFNode(val type: String, val content: List<ADFNode>?, val text: String?)
