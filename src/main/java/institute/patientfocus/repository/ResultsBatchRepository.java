package institute.patientfocus.repository;

import institute.patientfocus.domain.ResultsBatch;
import org.springframework.data.couchbase.repository.CouchbaseRepository;

/**
 * Spring Data Couchbase repository for the Tester entity.
 */
public interface ResultsBatchRepository extends CouchbaseRepository<ResultsBatch, String> {

}
