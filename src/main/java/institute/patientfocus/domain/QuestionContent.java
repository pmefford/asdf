package institute.patientfocus.domain;

import java.util.List;
import java.util.Objects;

/**
 * Created by pmefford on 10/3/15.
 */
public class QuestionContent {

    private String key;

    private String category;

    private String type;

    private List<QuestionOption> options;

    private List<LocalizedText> text;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<QuestionOption> getOptions() {
        return options;
    }

    public void setOptions(List<QuestionOption> options) {
        this.options = options;
    }

    public List<LocalizedText> getText() {
        return text;
    }

    public void setText(List<LocalizedText> text) {
        this.text = text;
    }


    @Override
    public String toString() {
        return "QuestionContent{" +
            "key='" + key + '\'' +
            ", category='" + category + '\'' +
            ", type='" + type + '\'' +
            ", options=" + options +
            ", text=" + text +
            '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, category, type, options, text);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final QuestionContent other = (QuestionContent) obj;
        return Objects.equals(this.key, other.key)
            && Objects.equals(this.category, other.category)
            && Objects.equals(this.type, other.type)
            && Objects.equals(this.options, other.options)
            && Objects.equals(this.text, other.text);
    }
}
