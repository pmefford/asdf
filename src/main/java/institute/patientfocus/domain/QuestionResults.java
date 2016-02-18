package institute.patientfocus.domain;

import com.google.common.collect.Maps;

import java.util.Map;
import java.util.Objects;

/**
 * Created by pmefford on 8/14/15.
 */
public class QuestionResults {
    private String key;
    private Map<String, AnswerResults> answers = Maps.newHashMap();

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Map<String, AnswerResults> getAnswers() {
        return answers;
    }

    public void setAnswers(Map<String, AnswerResults> answers) {
        this.answers = answers;
    }

    @Override
    public String toString() {
        return "QuestionResults{" +
                "key='" + key + '\'' +
                ", answers=" + answers +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, answers);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final QuestionResults other = (QuestionResults) obj;
        return Objects.equals(this.key, other.key)
                && Objects.equals(this.answers, other.answers);
    }
}
