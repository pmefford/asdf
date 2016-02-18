package institute.patientfocus.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.data.couchbase.core.mapping.Field;

import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;


/**
 * A SurveyFirst.
 */
@Document
public class SurveyResults implements Serializable {

    public static final String SEQ = "survey-results";

    @Id
    private String id;

    @Field("surveyName")
    private String surveyName;

    @Field("questions")
    private List<QuestionResults> questions = Collections.emptyList();

    @Field("updated")
    private Date updated;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<QuestionResults> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionResults> questions) {
        this.questions = questions;
    }

    public String getSurveyName() {
        return surveyName;
    }

    public void setSurveyName(String surveyName) {
        this.surveyName = surveyName;
    }


    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    @Override
    public String toString() {
        return "SurveyResults{" +
                "id='" + id + '\'' +
                ", surveyName='" + surveyName + '\'' +
                ", questions=" + questions +
                ", updated=" + updated +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, surveyName, questions, updated);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final SurveyResults other = (SurveyResults) obj;
        return Objects.equals(this.id, other.id)
                && Objects.equals(this.surveyName, other.surveyName)
                && Objects.equals(this.questions, other.questions)
                && Objects.equals(this.updated, other.updated);
    }
}
