package institute.patientfocus.repository;

import com.couchbase.client.protocol.views.Query;
import institute.patientfocus.domain.SurveyFirst;
import org.springframework.data.couchbase.core.view.View;
import org.springframework.data.couchbase.repository.CouchbaseRepository;

import java.util.List;

/**
 * Spring Data Couchbase repository for the Tester entity.
 */
public interface SurveyFirstRepository extends CouchbaseRepository<SurveyFirst, String> {
    @View(designDocument = "surveyfirst_old", viewName = "byNameAndBatch")
    List<SurveyFirst> findByNameAndBatch(Query query);

    @View(designDocument = "surveyfirst_old", viewName = "byName")
    List<SurveyFirst> findByName(Query query);

    @View(designDocument = "surveyfirst_old", viewName = "oneByReportId")
    List<SurveyFirst> findOneByReportId(Query query);
}
