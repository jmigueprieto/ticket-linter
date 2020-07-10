package me.mprieto.covidio.linter.services.validators

import org.slf4j.Logger
import org.springframework.stereotype.Service

@Service
class ValidatorService(private val log: Logger, val validators: List<Validator>) {

    fun validate(text: String): ValidationResult {
        val results = validators.map { validator -> validator.validate(text) }
        log.debug("Result of validating '{}' : {}", text, results)
        return ValidationResult(results.all { it.isValid }, results.flatMap { it.severities }, results.flatMap { it.messages })
    }
}
