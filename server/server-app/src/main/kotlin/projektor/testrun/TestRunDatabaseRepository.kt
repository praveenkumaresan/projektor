package projektor.testrun

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jooq.Configuration
import org.jooq.DSLContext
import org.simpleflatmapper.jdbc.JdbcMapperFactory
import org.slf4j.LoggerFactory
import projektor.database.generated.Tables.*
import projektor.database.generated.tables.daos.*
import projektor.incomingresults.mapper.toDB
import projektor.incomingresults.mapper.toTestRunSummary
import projektor.incomingresults.model.GroupedResults
import projektor.parser.model.TestSuite as ParsedTestSuite
import projektor.server.api.PublicId
import projektor.server.api.TestRun
import projektor.server.api.TestRunSummary
import projektor.util.addPrefixToFields

class TestRunDatabaseRepository(private val dslContext: DSLContext) : TestRunRepository {
    private val logger = LoggerFactory.getLogger(javaClass.canonicalName)

    private val testRunMapper = JdbcMapperFactory.newInstance()
            .addKeys("id", "test_suites_id")
            .ignorePropertyNotFound()
            .newMapper(TestRun::class.java)

    override suspend fun saveTestRun(publicId: PublicId, testSuites: List<ParsedTestSuite>) =
        withContext(Dispatchers.IO) {
            val testRunSummary = toTestRunSummary(publicId, testSuites)
            val testRunDB = testRunSummary.toDB()

            dslContext.transaction { configuration ->
                val testRunDao = TestRunDao(configuration)

                testRunDao.insert(testRunDB)

                saveTestSuites(testSuites, testRunDB.id, null, 0, configuration)

                logger.info("Finished inserting test run $publicId")
            }

            testRunSummary
        }

    override suspend fun saveGroupedTestRun(publicId: PublicId, groupedResults: GroupedResults) =
            withContext(Dispatchers.IO) {
                val testSuites = groupedResults.groupedTestSuites.flatMap { it.testSuites }
                val testRunSummary = toTestRunSummary(publicId, testSuites)
                val testRunDB = testRunSummary.toDB()

                logger.info("Starting inserting test run $publicId")

                dslContext.transaction { configuration ->
                    val testRunDao = TestRunDao(configuration)
                    val testSuiteGroupDao = TestSuiteGroupDao(configuration)

                    testRunDao.insert(testRunDB)

                    var testSuiteStartingIndex = 0

                    groupedResults.groupedTestSuites.forEach { groupedTestSuites ->
                        val testSuiteGroupDB = groupedTestSuites.toDB(testRunDB.id)
                        testSuiteGroupDao.insert(testSuiteGroupDB)

                        saveTestSuites(groupedTestSuites.testSuites, testRunDB.id, testSuiteGroupDB.id, testSuiteStartingIndex, configuration)

                        testSuiteStartingIndex += groupedTestSuites.testSuites.size
                    }
                }

                logger.info("Finished inserting test run $publicId")

                testRunSummary
            }

    private fun saveTestSuites(
        testSuites: List<ParsedTestSuite>,
        testRunId: Long,
        testGroupId: Long?,
        testSuiteStartingIndex: Int,
        configuration: Configuration
    ) {
        val testSuiteDao = TestSuiteDao(configuration)
        val testCaseDao = TestCaseDao(configuration)
        val testFailureDao = TestFailureDao(configuration)

        testSuites.forEachIndexed { testSuiteIdx, testSuite ->
            val testSuiteDB = testSuite.toDB(testRunId, testGroupId, testSuiteStartingIndex + testSuiteIdx + 1)
            testSuiteDao.insert(testSuiteDB)

            testSuite.testCases.forEachIndexed { testCaseIdx, testCase ->
                val testCaseDB = testCase.toDB(testSuiteDB.id, testCaseIdx + 1)
                testCaseDao.insert(testCaseDB)

                if (testCase.failure != null) {
                    val testFailureDB = testCase.failure.toDB(testCaseDB.id)
                    testFailureDao.insert(testFailureDB)
                }
            }
        }
    }

    override suspend fun fetchTestRun(publicId: PublicId): TestRun? =
            withContext(Dispatchers.IO) {
                val resultSet = dslContext
                        .select(TEST_RUN.PUBLIC_ID.`as`("id"))
                        .select(addPrefixToFields("summary", TEST_RUN.fields().toList()))
                        .select(addPrefixToFields("test_suites_", TEST_SUITE.fields().toList()))
                        .select(TEST_SUITE_GROUP.GROUP_LABEL.`as`("test_suites_group_label"))
                        .select(TEST_SUITE_GROUP.GROUP_NAME.`as`("test_suites_group_name"))
                        .from(TEST_RUN)
                        .leftOuterJoin(TEST_SUITE).on(TEST_SUITE.TEST_RUN_ID.eq(TEST_RUN.ID))
                        .leftOuterJoin(TEST_SUITE_GROUP).on(TEST_SUITE.TEST_SUITE_GROUP_ID.eq(TEST_SUITE_GROUP.ID))
                        .where(TEST_RUN.PUBLIC_ID.eq(publicId.id))
                        .fetchResultSet()

                val testRun: TestRun? = resultSet.use {
                    testRunMapper.stream(resultSet).findFirst().orElse(null)
                }

                testRun
            }

    override suspend fun fetchTestRunSummary(publicId: PublicId): TestRunSummary? =
            withContext(Dispatchers.IO) {
                dslContext
                        .select(TEST_RUN.PUBLIC_ID.`as`("id"))
                        .select(TEST_RUN.fields().filterNot { it.name == "id" }.toList())
                        .from(TEST_RUN)
                        .where(TEST_RUN.PUBLIC_ID.eq(publicId.id))
                        .fetchOneInto(TestRunSummary::class.java)
            }
}
