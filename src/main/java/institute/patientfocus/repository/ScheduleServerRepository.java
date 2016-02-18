package institute.patientfocus.repository;

import institute.patientfocus.domain.ScheduleServer;
import org.springframework.data.couchbase.repository.CouchbaseRepository;

/**
 * Created by pmefford on 8/30/15.
 */
public interface ScheduleServerRepository extends CouchbaseRepository<ScheduleServer, String> {
}
