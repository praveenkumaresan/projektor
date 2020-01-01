package projektor.incomingresults

import io.ktor.http.*
import io.ktor.server.testing.*
import io.ktor.util.KtorExperimentalAPI
import java.math.BigDecimal
import kotlin.test.*
import org.awaitility.kotlin.await
import org.awaitility.kotlin.untilNotNull
import projektor.ApplicationTestCase
import projektor.server.api.SaveResultsResponse
import strikt.api.expectThat
import strikt.assertions.*

@KtorExperimentalAPI
class SaveResultsApplicationTest : ApplicationTestCase() {
    @Test
    fun shouldParseRequestAndSaveResultsForPassingTest() {
        val requestBody = resultsXmlLoader.passing()

        withTestApplication(::createTestApplication) {
            handleRequest(HttpMethod.Post, "/results") {
                addHeader(HttpHeaders.ContentType, "text/plain")
                setBody(requestBody)
            }.apply {
                val resultsResponse = objectMapper.readValue(response.content, SaveResultsResponse::class.java)

                val publicId = resultsResponse.id
                assertNotNull(publicId)
                expectThat(resultsResponse.uri).isEqualTo("/tests/$publicId")

                val testRun = await untilNotNull { testRunDao.fetchOneByPublicId(publicId) }
                assertNotNull(testRun)

                expectThat(testRun.createdTimestamp).isNotNull()
                expectThat(testRun.totalTestCount).isEqualTo(1)
                expectThat(testRun.totalPassingCount).isEqualTo(1)
                expectThat(testRun.totalFailureCount).isEqualTo(0)
                expectThat(testRun.totalSkippedCount).isEqualTo(0)
                expectThat(testRun.passed).isTrue()
                expectThat(testRun.cumulativeDuration).isGreaterThan(BigDecimal.ZERO)
                expectThat(testRun.averageDuration).isGreaterThan(BigDecimal.ZERO)

                val testSuites = testSuiteDao.fetchByTestRunId(testRun.id)
                expectThat(testSuites).hasSize(1)

                val testCases = testCaseDao.fetchByTestSuiteId(testSuites[0].id)
                expectThat(testCases).hasSize(1)

                val testFailures = testFailureDao.fetchByTestCaseId(testCases[0].id)
                expectThat(testFailures).isEmpty()
            }
        }
    }

    @Test
    fun shouldParseRequestAndSaveResultsForFailingTest() {
        val requestBody = resultsXmlLoader.failing()

        withTestApplication(::createTestApplication) {
            handleRequest(HttpMethod.Post, "/results") {
                addHeader(HttpHeaders.ContentType, "text/plain")
                setBody(requestBody)
            }.apply {
                val resultsResponse = objectMapper.readValue(response.content, SaveResultsResponse::class.java)

                val publicId = resultsResponse.id
                assertNotNull(publicId)
                expectThat(resultsResponse.uri).isEqualTo("/tests/$publicId")

                val testRun = await untilNotNull { testRunDao.fetchOneByPublicId(publicId) }
                assertNotNull(testRun)

                expectThat(testRun.totalTestCount).isEqualTo(2)
                expectThat(testRun.totalPassingCount).isEqualTo(0)
                expectThat(testRun.totalFailureCount).isEqualTo(2)
                expectThat(testRun.totalSkippedCount).isEqualTo(0)
                expectThat(testRun.passed).isFalse()
                expectThat(testRun.cumulativeDuration).isGreaterThan(BigDecimal.ZERO)
                expectThat(testRun.averageDuration).isGreaterThan(BigDecimal.ZERO)

                val testSuites = testSuiteDao.fetchByTestRunId(testRun.id)
                expectThat(testSuites).hasSize(1)

                val testCases = testCaseDao.fetchByTestSuiteId(testSuites[0].id)
                expectThat(testCases).hasSize(2)

                val shouldFailTestCase = testCases.find { it.name == "should fail" }
                assertNotNull(shouldFailTestCase)

                expectThat(shouldFailTestCase.className).isEqualTo("FailingSpec")
                expectThat(shouldFailTestCase.packageName).isEqualTo("projektor.example.spock")

                val shouldFailTestFailures = testFailureDao.fetchByTestCaseId(shouldFailTestCase.id)
                expectThat(shouldFailTestFailures).hasSize(1)

                val shouldFailWithOutputTestCase = testCases.find { it.name == "should fail with output" }
                assertNotNull(shouldFailWithOutputTestCase)
                val shouldFailWithOutputTestFailures = testFailureDao.fetchByTestCaseId(shouldFailWithOutputTestCase.id)

                expectThat(shouldFailWithOutputTestFailures).hasSize(1)
            }
        }
    }

    @Test
    fun shouldParseRequestAndSaveResultsForBothPassingAndFailingTests() {
        val requestBody = listOf(resultsXmlLoader.passing(), resultsXmlLoader.failing()).joinToString("\n")

        withTestApplication(::createTestApplication) {
            handleRequest(HttpMethod.Post, "/results") {
                addHeader(HttpHeaders.ContentType, "text/plain")
                setBody(requestBody)
            }.apply {
                val resultsResponse = objectMapper.readValue(response.content, SaveResultsResponse::class.java)

                val publicId = resultsResponse.id
                assertNotNull(publicId)

                val testRun = await untilNotNull { testRunDao.fetchOneByPublicId(publicId) }
                assertNotNull(testRun)

                expectThat(testRun.totalTestCount).isEqualTo(3)
                expectThat(testRun.totalPassingCount).isEqualTo(1)
                expectThat(testRun.totalFailureCount).isEqualTo(2)
                expectThat(testRun.totalSkippedCount).isEqualTo(0)
                expectThat(testRun.passed).isFalse()
                expectThat(testRun.cumulativeDuration).isGreaterThan(BigDecimal.ZERO)
                expectThat(testRun.averageDuration).isGreaterThan(BigDecimal.ZERO)

                val testSuites = testSuiteDao.fetchByTestRunId(testRun.id)
                expectThat(testSuites).hasSize(2)

                val passingTestSuite = testSuites.find { it.className.contains("PassingSpec") }
                assertNotNull(passingTestSuite)
                expectThat(passingTestSuite.testCount).isEqualTo(1)
                expectThat(passingTestSuite.failureCount).isEqualTo(0)
                val passingSuiteTestCases = testCaseDao.fetchByTestSuiteId(passingTestSuite.id)
                expectThat(passingSuiteTestCases).hasSize(1)

                val failingTestSuite = testSuites.find { it.className.contains("FailingSpec") }
                assertNotNull(failingTestSuite)
                expectThat(failingTestSuite.testCount).isEqualTo(2)
                expectThat(failingTestSuite.failureCount).isEqualTo(2)
                val failingSuiteTestCases = testCaseDao.fetchByTestSuiteId(failingTestSuite.id)
                expectThat(failingSuiteTestCases.size).isEqualTo(2)
            }
        }
    }

    @Test
    fun shouldParseAndSaveResultsWithSystemOutAndSystemErr() {
        val requestBody = resultsXmlLoader.output()

        withTestApplication(::createTestApplication) {
            handleRequest(HttpMethod.Post, "/results") {
                addHeader(HttpHeaders.ContentType, "text/plain")
                setBody(requestBody)
            }.apply {
                val resultsResponse = objectMapper.readValue(response.content, SaveResultsResponse::class.java)

                val publicId = resultsResponse.id
                assertNotNull(publicId)

                val testRun = await untilNotNull { testRunDao.fetchOneByPublicId(publicId) }
                assertNotNull(testRun)

                val testSuites = testSuiteDao.fetchByTestRunId(testRun.id)
                expectThat(testSuites).hasSize(1)

                val testSuite = testSuites[0]
                expectThat(testSuite.className).isEqualTo("OutputSpec")
                expectThat(testSuite.hasSystemOut).isTrue()
                expectThat(testSuite.hasSystemErr).isTrue()

                val testCases = testCaseDao.fetchByTestSuiteId(testSuite.id)
                expectThat(testCases).hasSize(1)

                val testCase = testCases[0]
                expectThat(testCase.name).isEqualTo("should include system out and system err")
            }
        }
    }

    @Test
    fun shouldParseAndSaveResultsWithSomeSkippedTestCases() {
        val requestBody = resultsXmlLoader.someIgnored()

        withTestApplication(::createTestApplication) {
            handleRequest(HttpMethod.Post, "/results") {
                addHeader(HttpHeaders.ContentType, "text/plain")
                setBody(requestBody)
            }.apply {
                val resultsResponse = objectMapper.readValue(response.content, SaveResultsResponse::class.java)

                val publicId = resultsResponse.id
                assertNotNull(publicId)

                val testRun = await untilNotNull { testRunDao.fetchOneByPublicId(publicId) }
                assertNotNull(testRun)

                val testSuites = testSuiteDao.fetchByTestRunId(testRun.id)
                expectThat(testSuites).hasSize(1)

                val testCases = testCaseDao.fetchByTestSuiteId(testSuites[0].id)
                expectThat(testCases.size).isEqualTo(10)

                expectThat(testCases.find { it.name == "should run test case 1" }!!.skipped).isFalse()
                expectThat(testCases.find { it.name == "should run test case 2" }!!.skipped).isFalse()
                expectThat(testCases.find { it.name == "should not run test case 3" }!!.skipped).isTrue()
                expectThat(testCases.find { it.name == "should run test case 4" }!!.skipped).isFalse()
                expectThat(testCases.find { it.name == "should run test case 5" }!!.skipped).isFalse()
                expectThat(testCases.find { it.name == "should not run test case 6" }!!.skipped).isTrue()
                expectThat(testCases.find { it.name == "should not run test case 7" }!!.skipped).isTrue()
                expectThat(testCases.find { it.name == "should run test case 8" }!!.skipped).isFalse()
                expectThat(testCases.find { it.name == "should run test case 9" }!!.skipped).isFalse()
                expectThat(testCases.find { it.name == "should run test case 10" }!!.skipped).isFalse()
            }
        }
    }

    @Test
    fun shouldFilterOutTestSuitesThatHaveNoTestCases() {
        val requestBody = resultsXmlLoader.cypressResults().joinToString("\n")

        withTestApplication(::createTestApplication) {
            handleRequest(HttpMethod.Post, "/results") {
                addHeader(HttpHeaders.ContentType, "text/plain")
                setBody(requestBody)
            }.apply {
                expectThat(response.status()).isEqualTo(HttpStatusCode.OK)

                val resultsResponse = objectMapper.readValue(response.content, SaveResultsResponse::class.java)

                val publicId = resultsResponse.id
                assertNotNull(publicId)
                expectThat(resultsResponse.uri).isEqualTo("/tests/$publicId")

                val testRun = await untilNotNull { testRunDao.fetchOneByPublicId(publicId) }
                assertNotNull(testRun)

                val testSuites = testSuiteDao.fetchByTestRunId(testRun.id)

                testSuites.forEach { testSuite ->
                    val testCases = testCaseDao.fetchByTestSuiteId(testSuite.id)
                    expectThat(testCases).isNotEmpty()
                }
            }
        }
    }

    @Test
    fun whenMissingResultsShouldReturn400() {
        val requestBody = ""

        withTestApplication(::createTestApplication) {
            handleRequest(HttpMethod.Post, "/results") {
                addHeader(HttpHeaders.ContentType, "text/plain")
                setBody(requestBody)
            }.apply {
                expectThat(response.status()).isEqualTo(HttpStatusCode.BadRequest)
            }
        }
    }
}