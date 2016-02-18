package institute.patientfocus.repository;

import com.couchbase.client.protocol.views.Query;
import com.couchbase.client.protocol.views.ViewResponse;
import com.couchbase.client.protocol.views.ViewRow;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.couchbase.core.CouchbaseTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by pmefford on 8/24/15.
 */
@Repository
public class SurveyFirstStatsRepository {
    Logger logger = LoggerFactory.getLogger(SurveyFirstStatsRepository.class);

    @Autowired
    private CouchbaseTemplate couchbaseTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public List findByNameAndBatchAndOccasion(Query query) {
        ViewResponse viewResponse =
            couchbaseTemplate.queryView("surveyfirst", "reportByNameAndBatchAndOccasion", query);
        List<Map> results = new ArrayList<>();
        if (!StringUtils.isEmpty(viewResponse.toString())) {
            ViewRow row = viewResponse.iterator().next();

            try {
                Map map = objectMapper.readValue(row.getValue(), Map.class);
                results.add(map);
            } catch (IOException e) {
                logger.error("failed to map results", e);
            }
        }
        return results;
    }
}
