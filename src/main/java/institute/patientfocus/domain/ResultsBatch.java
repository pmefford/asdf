package institute.patientfocus.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.data.couchbase.core.mapping.Field;

import java.util.Objects;

/**
 * Created by pmefford on 8/28/15.
 */
@Document
public class ResultsBatch {

    @Id
    private String id;

    @Field("batchNumber")
    private Integer batchNumber = 0;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id + "-batch";
    }

    public Integer getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(Integer batchNumber) {
        this.batchNumber = batchNumber;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, batchNumber);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final ResultsBatch other = (ResultsBatch) obj;
        return Objects.equals(this.id, other.id)
            && Objects.equals(this.batchNumber, other.batchNumber);
    }
}
