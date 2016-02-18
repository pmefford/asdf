package institute.patientfocus.repository;

import institute.patientfocus.domain.Authority;
import org.springframework.data.couchbase.repository.CouchbaseRepository;

/**
 * Spring Data MongoDB repository for the Authority entity.
 */
public interface AuthorityRepository extends CouchbaseRepository<Authority, String> {
}
