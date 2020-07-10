package me.mprieto.covidio.linter.services.validators

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class UserStoryValidatorIntegrationTests {

    @Autowired
    private lateinit var validator: UserStoryValidator

    @Test
    fun `when text contains a user story expect validate to return true`() {
        val text = "*User Story*" +
                "As a user I want to see a dashboard so that I can get quick info " +
                "about ticket quality in my projects\n" +
                "*Acceptance Criteria*" +
                "User sees a dashboard" +
                "Dashboard shows the number of analyzed tickets, flagged tickets, etc.   " +
                "User sees those numbers broken down by project.   " +
                "User has the options of viewing more details.   " +
                "User has the option to trigger a full project analysis (all tickets are evaluated)."
        val result = validator.validate(text)
        assertTrue(result.isValid)
        assertTrue(result.messages.isEmpty())
        assertEquals(1, result.severities.size)
        assertEquals(Severity.SUCCESS, result.severities[0])
    }

    @Test
    fun `when text contains a simple user story (without benefit) expect validate to return true`() {
        val text = "As someone I want to see a dashboard"
        val result = validator.validate(text)
        assertTrue(result.isValid)
        assertFalse(result.messages.isEmpty())
        assertEquals("OK, but Benefit is missing.", result.messages[0])
        assertEquals(1, result.severities.size)
        assertEquals(Severity.WARNING, result.severities[0])
    }

    @Test
    fun `when text does not contains a user story expect validate to return false`() {
        val text = "Create database schema As a developer"
        val result = validator.validate(text)
        assertFalse(result.isValid)
        assertEquals(1, result.messages.size)
        assertEquals("Doesn't seem to contain a properly written User Story.", result.messages[0])
        assertEquals(1, result.severities.size)
        assertEquals(Severity.ERROR, result.severities[0])
    }
}
