package me.mprieto.covidio.linter.services.validators

import org.slf4j.Logger
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Service

/***
 * Checks if the text contains a User Story properly written i.e. "persona intent benefit"
 */
@Service
@Order(1)
class UserStoryValidator(val log: Logger) : Validator {

    // https://stackoverflow.com/questions/56905603/kotlin-regex-thread-safety
    private val regexStoryWithBenefit =
            "As (?<persona>.+),? ?I (want to|need to|have to)? (?<intent>.+),? ?(so that|in order to) (?<benefit>.+)?"
                    .toRegex(setOf(RegexOption.IGNORE_CASE))

    private val simpleStory = "As (?<persona>.+),? ?I (want to|need to|have to)? (?<intent>.+)"
            .toRegex(setOf(RegexOption.IGNORE_CASE))

    override fun validate(text: String): ValidationResult {
        log.debug("Analyzing story '{}'", text)
        val found = regexStoryWithBenefit.find(text) ?: simpleStory.find(text)
        if (found == null) {
            log.debug("The following text doesn't seem to contain a properly written user story: '$text'")
            return ValidationResult(false, "Doesn't seem to contain a properly written User Story.")
        }

        val persona = found.groups["persona"]
        val intent = found.groups["intent"]
        val benefit = if (found.groups.size == 4) null else found.groups["benefit"]

        // This data can be used to analyze stories
        log.debug("persona: $persona")
        log.debug("intent: $intent")
        log.debug("benefit: $benefit")

        return if (benefit == null) {
            ValidationResult(true, "OK, but Benefit is missing.")
        } else {
            ValidationResult(true)
        }
    }
}

/**
 * Checks if the text contains the words "acceptance criteria"
 */
@Service
@Order(2)
class AcceptanceCriteriaValidator : Validator {

    override fun validate(text: String): ValidationResult {
        return if (text.contains("acceptance criteria", true)) {
            ValidationResult(true)
        } else {
            ValidationResult(false, "Doesn't seem to explicitly list an Acceptance Criteria.")
        }

    }
}