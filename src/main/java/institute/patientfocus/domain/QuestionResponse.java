package institute.patientfocus.domain;

import java.util.Objects;

/**
 * Created by pmefford on 7/26/15.
 */
public class QuestionResponse {

    private String questionKey;
    private String category;
    private String answers;
    private String type;

    public String getQuestionKey() {
        return questionKey;
    }

    public void setQuestionKey(String questionKey) {
        this.questionKey = questionKey;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAnswers() {
        return answers;
    }

    public void setAnswers(String answers) {
        this.answers = answers;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(questionKey, category, answers, type);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final QuestionResponse other = (QuestionResponse) obj;
        return Objects.equals(this.questionKey, other.questionKey)
            && Objects.equals(this.category, other.category)
            && Objects.equals(this.answers, other.answers)
            && Objects.equals(this.type, other.type);
    }

    @Override
    public String toString() {
        return "QuestionResponse{" +
            "questionKey='" + questionKey + '\'' +
            ", category='" + category + '\'' +
            ", answers=" + answers +
            ", type='" + type + '\'' +
            '}';
    }
}
