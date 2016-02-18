package institute.patientfocus.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.data.couchbase.core.mapping.Field;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * Created by pmefford on 8/5/15.
 */
@Document
public class SequenceId implements Serializable {

    @Id
    @Field("id")
    private String key = UUID.randomUUID().toString();

    @Field("type")
    private String type;

    @Field("value")
    private Long value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, type, value);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final SequenceId other = (SequenceId) obj;
        return Objects.equals(this.key, other.key)
            && Objects.equals(this.type, other.type)
            && Objects.equals(this.value, other.value);
    }
}
