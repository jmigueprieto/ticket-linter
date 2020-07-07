package me.mprieto.covidio.linter.controllers

import com.atlassian.connect.spring.AtlassianHost
import com.atlassian.connect.spring.AtlassianHostRepository
import com.atlassian.connect.spring.AtlassianHostUser
import com.atlassian.connect.spring.internal.request.jwt.SelfAuthenticationTokenGenerator
import com.fasterxml.jackson.databind.ObjectMapper
import me.mprieto.covidio.linter.services.atlassian.JiraCloudService
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc
import java.util.*

@Suppress("SpringJavaInjectionPointsAutowiringInspection")
@SpringBootTest
@AutoConfigureMockMvc
class RestControllerTest {

    @Autowired
    protected lateinit var mapper: ObjectMapper

    @Autowired
    protected lateinit var mockMvc: MockMvc

    @Autowired
    protected lateinit var hostRepository: AtlassianHostRepository

    @Autowired
    protected lateinit var generator: SelfAuthenticationTokenGenerator

    @MockBean
    protected lateinit var jiraService: JiraCloudService

    protected lateinit var token: String

    @BeforeEach
    fun before() {
        token = generateToken()
    }

    @AfterEach
    fun after() {
        hostRepository.deleteAll()
    }

    protected fun saveHost() = hostRepository.save(AtlassianHost().apply {
        clientKey = "e681310b-b759-3a23-b926-e9fe40330000"
        publicKey = "g2jtbwUgXSLtbEwFl34ztegcowY4Di4FBjwCkYoH+FdeAFDhSyRFPSx+92aTF1FcH+BlAFP21Y0000000"
        sharedSecret = "7777aaaaa00000ddddddaaaaccccAAAAAAAAAA111444"
        baseUrl = "https://da-devil.atlassian.net"
        productType = "jira"
        description = "Atlassian JIRA at https://team-666.atlassian.net"
        isAddonInstalled = true
        createdDate = Calendar.getInstance()
        lastModifiedDate = Calendar.getInstance()
    })

    protected fun generateToken() = generator.createSelfAuthenticationToken(AtlassianHostUser
            .builder(saveHost()).withUserAccountId("12345")
            .build())!!

}