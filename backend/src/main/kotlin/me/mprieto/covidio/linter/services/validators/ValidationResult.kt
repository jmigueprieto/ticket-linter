package me.mprieto.covidio.linter.services.validators

data class ValidationResult(val isValid: Boolean, val messages: List<String>) {
    constructor(isValid: Boolean) : this(isValid, emptyList())
    constructor(isValid: Boolean, message: String) : this(isValid, listOf(message))
}