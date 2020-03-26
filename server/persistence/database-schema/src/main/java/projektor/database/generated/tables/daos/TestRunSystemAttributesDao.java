/*
 * This file is generated by jOOQ.
 */
package projektor.database.generated.tables.daos;


import java.util.List;

import javax.annotation.processing.Generated;

import org.jooq.Configuration;
import org.jooq.impl.DAOImpl;

import projektor.database.generated.tables.TestRunSystemAttributes;
import projektor.database.generated.tables.records.TestRunSystemAttributesRecord;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "https://www.jooq.org",
        "jOOQ version:3.13.1"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class TestRunSystemAttributesDao extends DAOImpl<TestRunSystemAttributesRecord, projektor.database.generated.tables.pojos.TestRunSystemAttributes, String> {

    /**
     * Create a new TestRunSystemAttributesDao without any configuration
     */
    public TestRunSystemAttributesDao() {
        super(TestRunSystemAttributes.TEST_RUN_SYSTEM_ATTRIBUTES, projektor.database.generated.tables.pojos.TestRunSystemAttributes.class);
    }

    /**
     * Create a new TestRunSystemAttributesDao with an attached configuration
     */
    public TestRunSystemAttributesDao(Configuration configuration) {
        super(TestRunSystemAttributes.TEST_RUN_SYSTEM_ATTRIBUTES, projektor.database.generated.tables.pojos.TestRunSystemAttributes.class, configuration);
    }

    @Override
    public String getId(projektor.database.generated.tables.pojos.TestRunSystemAttributes object) {
        return object.getTestRunPublicId();
    }

    /**
     * Fetch records that have <code>test_run_public_id BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<projektor.database.generated.tables.pojos.TestRunSystemAttributes> fetchRangeOfTestRunPublicId(String lowerInclusive, String upperInclusive) {
        return fetchRange(TestRunSystemAttributes.TEST_RUN_SYSTEM_ATTRIBUTES.TEST_RUN_PUBLIC_ID, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>test_run_public_id IN (values)</code>
     */
    public List<projektor.database.generated.tables.pojos.TestRunSystemAttributes> fetchByTestRunPublicId(String... values) {
        return fetch(TestRunSystemAttributes.TEST_RUN_SYSTEM_ATTRIBUTES.TEST_RUN_PUBLIC_ID, values);
    }

    /**
     * Fetch a unique record that has <code>test_run_public_id = value</code>
     */
    public projektor.database.generated.tables.pojos.TestRunSystemAttributes fetchOneByTestRunPublicId(String value) {
        return fetchOne(TestRunSystemAttributes.TEST_RUN_SYSTEM_ATTRIBUTES.TEST_RUN_PUBLIC_ID, value);
    }

    /**
     * Fetch records that have <code>pinned BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<projektor.database.generated.tables.pojos.TestRunSystemAttributes> fetchRangeOfPinned(Boolean lowerInclusive, Boolean upperInclusive) {
        return fetchRange(TestRunSystemAttributes.TEST_RUN_SYSTEM_ATTRIBUTES.PINNED, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>pinned IN (values)</code>
     */
    public List<projektor.database.generated.tables.pojos.TestRunSystemAttributes> fetchByPinned(Boolean... values) {
        return fetch(TestRunSystemAttributes.TEST_RUN_SYSTEM_ATTRIBUTES.PINNED, values);
    }
}