package institute.patientfocus.service;

import com.couchbase.client.protocol.views.Query;
import institute.patientfocus.domain.SequenceId;
import institute.patientfocus.repository.SequenceIdRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


/**
 * Created by pmefford on 8/5/15.
 */
@Service
public class SequenceService {

    @Autowired
    private SequenceIdRepository sequenceIdRepository;

    public String nextId(String type) {
        Query query = new Query();
        query.setKey(type);
        Optional<SequenceId> sequenceId = sequenceIdRepository.findOneByType(query).stream().findFirst();
        Long result = 0l;
        if (!sequenceId.isPresent()) {
            SequenceId newId = new SequenceId();
            newId.setType(type);
            newId.setValue(result);
            sequenceIdRepository.save(newId);
        } else {
            SequenceId seq = sequenceId.get();
            result = seq.getValue() + 1;
            seq.setValue(result);
            sequenceIdRepository.save(seq);
        }
        return type + "-" + result;
    }
}
