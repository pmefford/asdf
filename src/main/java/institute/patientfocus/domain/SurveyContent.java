package institute.patientfocus.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.data.couchbase.core.mapping.Field;

import java.util.List;
import java.util.Objects;

/**
 * Created by pmefford on 8/30/15.
 */
@Document
public class SurveyContent {
    public static final String SEQ = "survey-content";

    @Id
    @Field("id")
    private String id;

    @Field("name")
    private String name;

    @Field("occasions")
    private List<String> occasions;

    @Field("questions")
    private List<QuestionContent> questions;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getOccasions() {
        return occasions;
    }

    public void setOccasions(List<String> occasions) {
        this.occasions = occasions;
    }

    public List<QuestionContent> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionContent> questions) {
        this.questions = questions;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, occasions, questions);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final SurveyContent other = (SurveyContent) obj;
        return Objects.equals(this.name, other.name)
            && Objects.equals(this.occasions, other.occasions)
            && Objects.equals(this.questions, other.questions);
    }

    @Override
    public String toString() {
        return "SurveyContent{" +
            "id='" + id + '\'' +
            ", name='" + name + '\'' +
            ", occasions=" + occasions +
            ", questions=" + questions +
            '}';
    }
}
