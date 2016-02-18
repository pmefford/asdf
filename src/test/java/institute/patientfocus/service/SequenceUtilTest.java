package institute.patientfocus.service;

import institute.patientfocus.Application;
import institute.patientfocus.config.CouchbaseConfig;
import institute.patientfocus.domain.SequenceId;
import institute.patientfocus.repository.SequenceIdRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.inject.Inject;

/**
 * Created by pmefford on 8/5/15.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
@Import(CouchbaseConfig.class)
public class SequenceUtilTest {


    @Inject
    private SequenceIdRepository sequenceIdRepository;


    @Test
    public void createSequence() {
        SequenceId sequenceId = new SequenceId();
        sequenceId.setType("surveyFirst");
        sequenceId.setValue(0l);

        sequenceIdRepository.save(sequenceId);

    }

    @Test
    public void testFindByType() {
        //given
        String type = "surveyFirst";
    }


}
