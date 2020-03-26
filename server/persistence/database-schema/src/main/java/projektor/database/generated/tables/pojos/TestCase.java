/*
 * This file is generated by jOOQ.
 */
package projektor.database.generated.tables.pojos;


import java.io.Serializable;
import java.math.BigDecimal;

import javax.annotation.processing.Generated;


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
public class TestCase implements Serializable {

    private static final long serialVersionUID = -510972921;

    private Long       id;
    private Long       testSuiteId;
    private Integer    idx;
    private String     name;
    private String     packageName;
    private String     className;
    private BigDecimal duration;
    private Boolean    passed;
    private Boolean    skipped;

    public TestCase() {}

    public TestCase(TestCase value) {
        this.id = value.id;
        this.testSuiteId = value.testSuiteId;
        this.idx = value.idx;
        this.name = value.name;
        this.packageName = value.packageName;
        this.className = value.className;
        this.duration = value.duration;
        this.passed = value.passed;
        this.skipped = value.skipped;
    }

    public TestCase(
        Long       id,
        Long       testSuiteId,
        Integer    idx,
        String     name,
        String     packageName,
        String     className,
        BigDecimal duration,
        Boolean    passed,
        Boolean    skipped
    ) {
        this.id = id;
        this.testSuiteId = testSuiteId;
        this.idx = idx;
        this.name = name;
        this.packageName = packageName;
        this.className = className;
        this.duration = duration;
        this.passed = passed;
        this.skipped = skipped;
    }

    public Long getId() {
        return this.id;
    }

    public TestCase setId(Long id) {
        this.id = id;
        return this;
    }

    public Long getTestSuiteId() {
        return this.testSuiteId;
    }

    public TestCase setTestSuiteId(Long testSuiteId) {
        this.testSuiteId = testSuiteId;
        return this;
    }

    public Integer getIdx() {
        return this.idx;
    }

    public TestCase setIdx(Integer idx) {
        this.idx = idx;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public TestCase setName(String name) {
        this.name = name;
        return this;
    }

    public String getPackageName() {
        return this.packageName;
    }

    public TestCase setPackageName(String packageName) {
        this.packageName = packageName;
        return this;
    }

    public String getClassName() {
        return this.className;
    }

    public TestCase setClassName(String className) {
        this.className = className;
        return this;
    }

    public BigDecimal getDuration() {
        return this.duration;
    }

    public TestCase setDuration(BigDecimal duration) {
        this.duration = duration;
        return this;
    }

    public Boolean getPassed() {
        return this.passed;
    }

    public TestCase setPassed(Boolean passed) {
        this.passed = passed;
        return this;
    }

    public Boolean getSkipped() {
        return this.skipped;
    }

    public TestCase setSkipped(Boolean skipped) {
        this.skipped = skipped;
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final TestCase other = (TestCase) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        }
        else if (!id.equals(other.id))
            return false;
        if (testSuiteId == null) {
            if (other.testSuiteId != null)
                return false;
        }
        else if (!testSuiteId.equals(other.testSuiteId))
            return false;
        if (idx == null) {
            if (other.idx != null)
                return false;
        }
        else if (!idx.equals(other.idx))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        }
        else if (!name.equals(other.name))
            return false;
        if (packageName == null) {
            if (other.packageName != null)
                return false;
        }
        else if (!packageName.equals(other.packageName))
            return false;
        if (className == null) {
            if (other.className != null)
                return false;
        }
        else if (!className.equals(other.className))
            return false;
        if (duration == null) {
            if (other.duration != null)
                return false;
        }
        else if (!duration.equals(other.duration))
            return false;
        if (passed == null) {
            if (other.passed != null)
                return false;
        }
        else if (!passed.equals(other.passed))
            return false;
        if (skipped == null) {
            if (other.skipped != null)
                return false;
        }
        else if (!skipped.equals(other.skipped))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
        result = prime * result + ((this.testSuiteId == null) ? 0 : this.testSuiteId.hashCode());
        result = prime * result + ((this.idx == null) ? 0 : this.idx.hashCode());
        result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
        result = prime * result + ((this.packageName == null) ? 0 : this.packageName.hashCode());
        result = prime * result + ((this.className == null) ? 0 : this.className.hashCode());
        result = prime * result + ((this.duration == null) ? 0 : this.duration.hashCode());
        result = prime * result + ((this.passed == null) ? 0 : this.passed.hashCode());
        result = prime * result + ((this.skipped == null) ? 0 : this.skipped.hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("TestCase (");

        sb.append(id);
        sb.append(", ").append(testSuiteId);
        sb.append(", ").append(idx);
        sb.append(", ").append(name);
        sb.append(", ").append(packageName);
        sb.append(", ").append(className);
        sb.append(", ").append(duration);
        sb.append(", ").append(passed);
        sb.append(", ").append(skipped);

        sb.append(")");
        return sb.toString();
    }
}