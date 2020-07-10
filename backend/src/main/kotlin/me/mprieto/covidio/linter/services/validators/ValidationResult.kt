package me.mprieto.covidio.linter.services.validators

enum class Severity { ERROR, SUCCESS, WARNING }

data class ValidationResult(val isValid: Boolean, val severities: List<Severity>, val messages: List<String>) {
    constructor(isValid: Boolean, severity: Severity) : this(isValid, listOf(severity), emptyList())
    constructor(isValid: Boolean, severity: Severity, message: String) : this(isValid, listOf(severity), listOf(message))
}
