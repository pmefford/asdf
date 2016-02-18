package institute.patientfocus.repository;

import institute.patientfocus.domain.SurveyContent;
import org.springframework.data.couchbase.repository.CouchbaseRepository;

/**
 * Spring Data Couchbase repository for the Tester entity.
 */
public interface SurveyContentRepository extends CouchbaseRepository<SurveyContent, String> {

}
