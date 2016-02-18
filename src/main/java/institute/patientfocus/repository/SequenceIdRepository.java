package institute.patientfocus.repository;

import com.couchbase.client.protocol.views.Query;
import institute.patientfocus.domain.SequenceId;
import org.springframework.data.couchbase.repository.CouchbaseRepository;

import java.util.List;

/**
 * Created by pmefford on 8/5/15.
 */
public interface SequenceIdRepository extends CouchbaseRepository<SequenceId, String> {
    List<SequenceId> findOneByType(Query typeQuery);
}
