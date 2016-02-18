package institute.patientfocus.service;

import institute.patientfocus.domain.ResultsBatch;
import institute.patientfocus.repository.ResultsBatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by pmefford on 8/28/15.
 */
@Service
public class BatchService {

    @Autowired
    private ResultsBatchRepository batchRepository;

    public Integer nextBatchNumber(String name) {
        ResultsBatch resultsBatch = batchRepository.findOne(name + "-batch");
        Integer nextBatch;
        if (resultsBatch != null) {
            Integer current = resultsBatch.getBatchNumber();
            nextBatch = current + 1;
        } else {
            resultsBatch = new ResultsBatch();
            resultsBatch.setId(name);
            nextBatch = 1;
        }
        resultsBatch.setBatchNumber(nextBatch);
        batchRepository.save(resultsBatch);
        return nextBatch;
    }

    public Integer currentBatch(String name) {
        ResultsBatch resultsBatch = batchRepository.findOne(name + "-batch");
        Integer output;
        if (resultsBatch != null) {
            output = resultsBatch.getBatchNumber();
        } else {
            output = 0;
        }
        return output;
    }
}
