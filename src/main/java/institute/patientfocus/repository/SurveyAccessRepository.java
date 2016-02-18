package institute.patientfocus.repository;

import com.couchbase.client.protocol.views.Query;
import institute.patientfocus.domain.SurveyAccess;
import org.springframework.data.couchbase.core.view.View;
import org.springframework.data.couchbase.repository.CouchbaseRepository;

import java.util.List;

/**
 * Spring Data Couchbase repository for the Tester entity.
 */
public interface SurveyAccessRepository extends CouchbaseRepository<SurveyAccess, String> {
    @View(designDocument = "surveyaccess", viewName = "oneByAccessKey")
    List<SurveyAccess> findOneByAccessKey(Query query);
}
