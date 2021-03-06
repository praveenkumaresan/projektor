package projektor.metrics

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.*
import io.ktor.http.HttpMethod
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.withTestApplication
import io.ktor.util.KtorExperimentalAPI
import org.awaitility.kotlin.await
import org.awaitility.kotlin.until
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import projektor.ApplicationTestCase
import projektor.incomingresults.randomPublicId

@KtorExperimentalAPI
class MetricsApplicationTest : ApplicationTestCase() {

    private val wireMockServer = WireMockServer(8095)

    @BeforeEach
    fun start() {
        wireMockServer.start()
    }

    @AfterEach
    fun stop() {
        wireMockServer.stop()
    }

    @Test
    fun `when metrics enabled should publish to InfluxDB`() {
        metricsEnabled = true
        metricsPort = 8095

        val publicId = randomPublicId()

        wireMockServer.stubFor(post(urlMatching("/query.*")).willReturn(aResponse().withStatus(200)))
        wireMockServer.stubFor(post(urlMatching("/write.*")).willReturn(aResponse().withStatus(200)))

        withTestApplication(::createTestApplication) {
            handleRequest(HttpMethod.Get, "/run/$publicId") {
                testRunDBGenerator.createTestRun(
                        publicId,
                        listOf()
                )
            }.apply {

                await until {
                    val createMetricsDatabaseRequests = wireMockServer.findAll(postRequestedFor(urlMatching("/query.*")))

                    createMetricsDatabaseRequests.size > 0
                }
            }
        }
    }
}
