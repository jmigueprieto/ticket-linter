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
                     val fields: IssueFields)

    data class IssueType(val id: String,
                         val name: String,
                         val description: String)

    data class IssueFields(val issuetype: IssueType,
                           val summary: String,
                           val description: Map<String, Any>) {

        val descriptionText: String by lazy {
            extractText()
        }

        private fun extractText(): String {
            val buff = StringBuilder()
            extractText(buff, description)
            return buff.toString()
        }

        //FIXME this needs refactoring, remove @Suppress
        //SEE https://developer.atlassian.com/cloud/jira/platform/apis/document/structure/
        @Suppress("UNCHECKED_CAST")
        private fun extractText(buff: StringBuilder, ads: Map<String, Any>) {
            if (ads.containsKey("content")) {
                val content = ads.getValue("content") as List<Map<String, Any>>
                for (c in content) {
                    extractText(buff, c)
                }
            } else {
                if (ads.containsKey("type") && ads.getValue("type") == "text") {
                    buff.append(ads["text"] as String)
                    // FIXME is this necessary
                    buff.append(' ')
                }
            }
        }
    }

    data class Page<T>(val data: T, val total: Int)

}