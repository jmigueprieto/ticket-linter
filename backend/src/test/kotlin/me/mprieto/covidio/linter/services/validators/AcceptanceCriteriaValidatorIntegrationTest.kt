package me.mprieto.covidio.linter.services.validators

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class AcceptanceCriteriaValidatorIntegrationTest {

    @Autowired
    private lateinit var validator: AcceptanceCriteriaValidator

    @Test
    fun `when text contains "Acceptance Criteria" expect validate to return true`() {
        val text = "Something, something.... \n" +
                "Acceptance Criteria ..." +
                "blah"
        val result = validator.validate(text)
        assertTrue(result.isValid)
        assertEquals(1, result.severities.size)
        assertEquals(Severity.SUCCESS, result.severities[0])
        assertTrue(result.messages.isEmpty())
    }

    @Test
    fun `when text doesn't contain "Acceptance Criteria" expect validate to return false`() {
        val text = "Something, something.... \n" +
                "blah"
        val result = validator.validate(text)
        assertFalse(result.isValid)
        assertFalse(result.messages.isEmpty())
        assertEquals(1, result.severities.size)
        assertEquals(Severity.ERROR, result.severities[0])
        assertEquals("Doesn't seem to explicitly list an Acceptance Criteria.", result.messages[0])
    }
}
