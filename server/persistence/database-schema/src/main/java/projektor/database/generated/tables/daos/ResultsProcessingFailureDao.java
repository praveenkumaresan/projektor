/*
 * This file is generated by jOOQ.
 */
package projektor.database.generated.tables.daos;


import java.util.List;

import javax.annotation.processing.Generated;

import org.jooq.Configuration;
import org.jooq.impl.DAOImpl;

import projektor.database.generated.tables.ResultsProcessingFailure;
import projektor.database.generated.tables.records.ResultsProcessingFailureRecord;


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
public class ResultsProcessingFailureDao extends DAOImpl<ResultsProcessingFailureRecord, projektor.database.generated.tables.pojos.ResultsProcessingFailure, String> {

    /**
     * Create a new ResultsProcessingFailureDao without any configuration
     */
    public ResultsProcessingFailureDao() {
        super(ResultsProcessingFailure.RESULTS_PROCESSING_FAILURE, projektor.database.generated.tables.pojos.ResultsProcessingFailure.class);
    }

    /**
     * Create a new ResultsProcessingFailureDao with an attached configuration
     */
    public ResultsProcessingFailureDao(Configuration configuration) {
        super(ResultsProcessingFailure.RESULTS_PROCESSING_FAILURE, projektor.database.generated.tables.pojos.ResultsProcessingFailure.class, configuration);
    }

    @Override
    public String getId(projektor.database.generated.tables.pojos.ResultsProcessingFailure object) {
        return object.getPublicId();
    }

    /**
     * Fetch records that have <code>public_id BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<projektor.database.generated.tables.pojos.ResultsProcessingFailure> fetchRangeOfPublicId(String lowerInclusive, String upperInclusive) {
        return fetchRange(ResultsProcessingFailure.RESULTS_PROCESSING_FAILURE.PUBLIC_ID, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>public_id IN (values)</code>
     */
    public List<projektor.database.generated.tables.pojos.ResultsProcessingFailure> fetchByPublicId(String... values) {
        return fetch(ResultsProcessingFailure.RESULTS_PROCESSING_FAILURE.PUBLIC_ID, values);
    }

    /**
     * Fetch a unique record that has <code>public_id = value</code>
     */
    public projektor.database.generated.tables.pojos.ResultsProcessingFailure fetchOneByPublicId(String value) {
        return fetchOne(ResultsProcessingFailure.RESULTS_PROCESSING_FAILURE.PUBLIC_ID, value);
    }

    /**
     * Fetch records that have <code>body BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<projektor.database.generated.tables.pojos.ResultsProcessingFailure> fetchRangeOfBody(String lowerInclusive, String upperInclusive) {
        return fetchRange(ResultsProcessingFailure.RESULTS_PROCESSING_FAILURE.BODY, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>body IN (values)</code>
     */
    public List<projektor.database.generated.tables.pojos.ResultsProcessingFailure> fetchByBody(String... values) {
        return fetch(ResultsProcessingFailure.RESULTS_PROCESSING_FAILURE.BODY, values);
    }
}