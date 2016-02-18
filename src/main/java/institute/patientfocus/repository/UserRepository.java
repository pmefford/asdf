package institute.patientfocus.repository;

import com.couchbase.client.protocol.views.Query;
import institute.patientfocus.domain.User;
import org.joda.time.DateTime;
import org.springframework.data.couchbase.repository.CouchbaseRepository;

import java.util.List;

/**
 * Spring Data MongoDB repository for the User entity.
 */
public interface UserRepository extends CouchbaseRepository<User, String> {

    List<User> findOneByActivationKey(Query activationKey);

    List<User> findAllByActivatedIsFalseAndCreatedDateBefore(DateTime dateTime);

    List<User> findOneByResetKey(Query resetKey);

    List<User> findOneByEmail(Query email);

    List<User> findOneByLogin(Query login);

    @Override
    void delete(User t);

}
