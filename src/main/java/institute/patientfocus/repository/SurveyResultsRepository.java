package institute.patientfocus.repository;

import com.couchbase.client.protocol.views.Query;
import institute.patientfocus.domain.SurveyResults;
import org.springframework.data.couchbase.repository.CouchbaseRepository;

import java.util.List;

/**
 * Spring Data Couchbase repository for the Tester entity.
 */
public interface SurveyResultsRepository extends CouchbaseRepository<SurveyResults, String> {

    List<SurveyResults> findByName(Query query);

    List<SurveyResults> findByOccasion(Query query);
}
