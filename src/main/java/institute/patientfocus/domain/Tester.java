package institute.patientfocus.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.data.couchbase.core.mapping.Field;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;


/**
 * A Tester.
 */
@Document
public class Tester implements Serializable {

    @Id
    @Field("id")
    private String id = UUID.randomUUID().toString();

    @Field("field1")
    private Integer field1;

    @Field("field2")
    private String field2;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getField1() {
        return field1;
    }

    public void setField1(Integer field1) {
        this.field1 = field1;
    }

    public String getField2() {
        return field2;
    }

    public void setField2(String field2) {
        this.field2 = field2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Tester tester = (Tester) o;

        if (!Objects.equals(id, tester.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Tester{" +
            "id=" + id +
            ", field1='" + field1 + "'" +
            ", field2='" + field2 + "'" +
            '}';
    }
}
