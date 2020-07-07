package me.mprieto.covidio.linter.services.atlassian

class Jira {
    data class Project(val id: String,
                       val key: String,
                       val name: String,
                       val self: String,
                       val projectTypeKey: String,
                       val style: String,
                       val avatarUrls: Map<String, String>)

    data class IssueSearch(val startAt: Int, val maxResults: Int, val total: Int, val issues: List<Issue>)

    data class Issue(val id: String,
                     val key: String,
                     val self: String,
                     private val fields: IssueFields) {

        val summary: String
            get() = fields.summary

        val descriptionText: String by lazy {
            extractText()
        }

        private fun extractText(): String {
            if (fields.description == null) {
                return ""
            }
            val buff = StringBuilder()
            extractText(buff, fields.description)
            return buff.toString()
        }

        private fun extractText(buff: StringBuilder, node: ADFNode) {
            if (node.type == "text") {
                buff.append(node.text)
                return
            }

            node.content?.forEach {
                extractText(buff, it)
                if (it.type == "paragraph") {
                    buff.append('\n')
                }
            }
        }
    }

    data class IssueType(val id: String,
                         val name: String,
                         val description: String)

    data class IssueFields(val issuetype: IssueType,
                           val summary: String,
                           val description: ADFNode?)

}
