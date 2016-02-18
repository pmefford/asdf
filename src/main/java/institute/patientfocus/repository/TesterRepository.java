package institute.patientfocus.repository;

import institute.patientfocus.domain.Tester;
import org.springframework.data.couchbase.repository.CouchbaseRepository;

/**
 * Spring Data MongoDB repository for the Tester entity.
 */
public interface TesterRepository extends CouchbaseRepository<Tester, String> {

}
