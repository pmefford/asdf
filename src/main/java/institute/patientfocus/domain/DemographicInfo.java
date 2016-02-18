package institute.patientfocus.domain;

/**
 * Created by pmefford on 10/31/15.
 */
public class DemographicInfo {
    private String email;
    private String country;
    private String industry;
    private String organization;
    private String orgSize;
    private String jobFunction;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getOrgSize() {
        return orgSize;
    }

    public void setOrgSize(String orgSize) {
        this.orgSize = orgSize;
    }

    public String getJobFunction() {
        return jobFunction;
    }

    public void setJobFunction(String jobFunction) {
        this.jobFunction = jobFunction;
    }
}
