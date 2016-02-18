package institute.patientfocus.domain;

import java.util.Objects;

/**
 * Created by pmefford on 2/3/16.
 */
public class AnswerResults {
    private String value;
    private Integer count;
    private Integer sum;


    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getSum() {
        return sum;
    }

    public void setSum(Integer sum) {
        this.sum = sum;
    }

    @Override
    public String toString() {
        return "AnswerResults{" +
                "value='" + value + '\'' +
                ", count=" + count +
                ", sum=" + sum +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, count, sum);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final AnswerResults other = (AnswerResults) obj;
        return Objects.equals(this.value, other.value)
                && Objects.equals(this.count, other.count)
                && Objects.equals(this.sum, other.sum);
    }
}
