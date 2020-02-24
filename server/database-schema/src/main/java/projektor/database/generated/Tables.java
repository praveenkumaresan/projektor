/*
 * This file is generated by jOOQ.
 */
package projektor.database.generated;


import javax.annotation.Generated;

import projektor.database.generated.tables.ResultsProcessing;
import projektor.database.generated.tables.TestCase;
import projektor.database.generated.tables.TestFailure;
import projektor.database.generated.tables.TestRun;
import projektor.database.generated.tables.TestRunAttachment;
import projektor.database.generated.tables.TestSuite;
import projektor.database.generated.tables.TestSuiteGroup;


/**
 * Convenience access to all tables in public
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.11.11"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Tables {

    /**
     * The table <code>public.results_processing</code>.
     */
    public static final ResultsProcessing RESULTS_PROCESSING = projektor.database.generated.tables.ResultsProcessing.RESULTS_PROCESSING;

    /**
     * The table <code>public.test_case</code>.
     */
    public static final TestCase TEST_CASE = projektor.database.generated.tables.TestCase.TEST_CASE;

    /**
     * The table <code>public.test_failure</code>.
     */
    public static final TestFailure TEST_FAILURE = projektor.database.generated.tables.TestFailure.TEST_FAILURE;

    /**
     * The table <code>public.test_run</code>.
     */
    public static final TestRun TEST_RUN = projektor.database.generated.tables.TestRun.TEST_RUN;

    /**
     * The table <code>public.test_run_attachment</code>.
     */
    public static final TestRunAttachment TEST_RUN_ATTACHMENT = projektor.database.generated.tables.TestRunAttachment.TEST_RUN_ATTACHMENT;

    /**
     * The table <code>public.test_suite</code>.
     */
    public static final TestSuite TEST_SUITE = projektor.database.generated.tables.TestSuite.TEST_SUITE;

    /**
     * The table <code>public.test_suite_group</code>.
     */
    public static final TestSuiteGroup TEST_SUITE_GROUP = projektor.database.generated.tables.TestSuiteGroup.TEST_SUITE_GROUP;
}
