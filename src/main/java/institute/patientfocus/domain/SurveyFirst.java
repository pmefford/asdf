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
public class SurveyFirst implements Serializable {
    public static final String SEQ = "survey-first";

    @Id
    @Field("id")
    private String id;

    @Field("name")
    private String name;

    @Field("occasion")
    private String occasion;

    @Field("accessKey")
    private String accessKey;

    @Field("questions")
    private List<QuestionResponse> questionList = Collections.emptyList();

    @Field("batch")
    private Integer batch = 0;

    @Field("reportId")
    private String reportId;

    @Field("updated")
    private Date updated;

    @Field
    private DemographicInfo demographicInfo;

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
        System.out.println("setting name: " + name);
        this.name = name;
    }

    public String getOccasion() {
        return occasion;
    }

    public void setOccasion(String occasion) {
        System.out.println("setting occasion: " + occasion);
        this.occasion = occasion;
    }

    public Integer getBatch() {
        return batch;
    }

    public void setBatch(Integer batch) {
        System.out.println("setting batch: " + batch);
        this.batch = batch;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        System.out.println("setting updated: " + updated);
        this.updated = updated;
    }

    public List<QuestionResponse> getQuestionList() {
        return questionList;
    }

    public void setQuestionList(List<QuestionResponse> questionList) {
        this.questionList = questionList;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public DemographicInfo getDemographicInfo() {
        return demographicInfo;
    }

    public void setDemographicInfo(DemographicInfo demographicInfo) {
        this.demographicInfo = demographicInfo;
    }

    @Override
    public String toString() {
        return "SurveyFirst{" +
            "id='" + id + '\'' +
            ", name='" + name + '\'' +
            ", occasion='" + occasion + '\'' +
            ", accessKey='" + accessKey + '\'' +
            ", questionList=" + questionList +
            ", batch=" + batch +
            ", reportId='" + reportId + '\'' +
            ", updated=" + updated +
            '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, occasion, accessKey, questionList, batch, reportId, updated);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final SurveyFirst other = (SurveyFirst) obj;
        return Objects.equals(this.id, other.id)
            && Objects.equals(this.name, other.name)
            && Objects.equals(this.occasion, other.occasion)
            && Objects.equals(this.accessKey, other.accessKey)
            && Objects.equals(this.questionList, other.questionList)
            && Objects.equals(this.batch, other.batch)
            && Objects.equals(this.reportId, other.reportId)
            && Objects.equals(this.updated, other.updated);
    }
}
