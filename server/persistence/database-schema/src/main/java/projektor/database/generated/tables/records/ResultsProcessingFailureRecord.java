/*
 * This file is generated by jOOQ.
 */
package projektor.database.generated.tables.records;


import javax.annotation.processing.Generated;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.Row2;
import org.jooq.impl.UpdatableRecordImpl;

import projektor.database.generated.tables.ResultsProcessingFailure;


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
public class ResultsProcessingFailureRecord extends UpdatableRecordImpl<ResultsProcessingFailureRecord> implements Record2<String, String> {

    private static final long serialVersionUID = -591957807;

    /**
     * Setter for <code>public.results_processing_failure.public_id</code>.
     */
    public ResultsProcessingFailureRecord setPublicId(String value) {
        set(0, value);
        return this;
    }

    /**
     * Getter for <code>public.results_processing_failure.public_id</code>.
     */
    public String getPublicId() {
        return (String) get(0);
    }

    /**
     * Setter for <code>public.results_processing_failure.body</code>.
     */
    public ResultsProcessingFailureRecord setBody(String value) {
        set(1, value);
        return this;
    }

    /**
     * Getter for <code>public.results_processing_failure.body</code>.
     */
    public String getBody() {
        return (String) get(1);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<String> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record2 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row2<String, String> fieldsRow() {
        return (Row2) super.fieldsRow();
    }

    @Override
    public Row2<String, String> valuesRow() {
        return (Row2) super.valuesRow();
    }

    @Override
    public Field<String> field1() {
        return ResultsProcessingFailure.RESULTS_PROCESSING_FAILURE.PUBLIC_ID;
    }

    @Override
    public Field<String> field2() {
        return ResultsProcessingFailure.RESULTS_PROCESSING_FAILURE.BODY;
    }

    @Override
    public String component1() {
        return getPublicId();
    }

    @Override
    public String component2() {
        return getBody();
    }

    @Override
    public String value1() {
        return getPublicId();
    }

    @Override
    public String value2() {
        return getBody();
    }

    @Override
    public ResultsProcessingFailureRecord value1(String value) {
        setPublicId(value);
        return this;
    }

    @Override
    public ResultsProcessingFailureRecord value2(String value) {
        setBody(value);
        return this;
    }

    @Override
    public ResultsProcessingFailureRecord values(String value1, String value2) {
        value1(value1);
        value2(value2);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached ResultsProcessingFailureRecord
     */
    public ResultsProcessingFailureRecord() {
        super(ResultsProcessingFailure.RESULTS_PROCESSING_FAILURE);
    }

    /**
     * Create a detached, initialised ResultsProcessingFailureRecord
     */
    public ResultsProcessingFailureRecord(String publicId, String body) {
        super(ResultsProcessingFailure.RESULTS_PROCESSING_FAILURE);

        set(0, publicId);
        set(1, body);
    }
}
