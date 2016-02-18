package institute.patientfocus.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.data.couchbase.core.mapping.Field;

import java.io.Serializable;
import java.util.Objects;

/**
 * Created by pmefford on 10/27/15.
 */
@Document
public class SurveyAccess implements Serializable {
    public static final String SEQ = "survey-access";

    @Id
    private String id;

    @Field
    private String surveyName;

    @Field
    private String accessKey;

    @Field
    private String source;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSurveyName() {
        return surveyName;
    }

    public void setSurveyName(String surveyName) {
        this.surveyName = surveyName;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @Override
    public String toString() {
        return "SurveyAccess{" +
            "id='" + id + '\'' +
            ", surveyName='" + surveyName + '\'' +
            ", accessKey='" + accessKey + '\'' +
            ", source='" + source + '\'' +
            '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, surveyName, accessKey, source);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final SurveyAccess other = (SurveyAccess) obj;
        return Objects.equals(this.id, other.id)
            && Objects.equals(this.surveyName, other.surveyName)
            && Objects.equals(this.accessKey, other.accessKey)
            && Objects.equals(this.source, other.source);
    }
}
