package me.mprieto.covidio.linter.controllers

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.getForObject
import org.springframework.boot.web.server.LocalServerPort


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class AppManifestIntegrationTest {

    @LocalServerPort
    private var port = 0

    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    @Test
    fun `when getting the root expect the atlassian-connect app descriptor`() {
        val response: Map<String, Any>? = restTemplate.getForObject("http://localhost:$port/")
        assertThat(response).isNotNull
        val descriptor = response!!
        println(descriptor)
        assertThat(descriptor).containsKeys("name",
                "description",
                "key",
                "baseUrl",
                "vendor",
                "authentication",
                "lifecycle",
                "modules")
        @Suppress("UNCHECKED_CAST")
        val authentication = descriptor["authentication"] as Map<String, String>
        assertThat(authentication["type"]).isEqualTo("jwt")
    }
}
