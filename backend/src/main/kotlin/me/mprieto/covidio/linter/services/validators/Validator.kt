package me.mprieto.covidio.linter.services.validators

interface Validator {

    fun validate(text: String): ValidationResult
}
