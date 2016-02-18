package institute.patientfocus.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.couchbase.client.protocol.views.ComplexKey;
import com.couchbase.client.protocol.views.Query;
import institute.patientfocus.domain.*;
import institute.patientfocus.repository.SurveyAccessRepository;
import institute.patientfocus.repository.SurveyContentRepository;
import institute.patientfocus.repository.SurveyFirstRepository;
import institute.patientfocus.service.MailService;
import institute.patientfocus.service.ReportingService;
import institute.patientfocus.service.SequenceService;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URISyntaxException;
import java.util.*;

/**
 * Created by pmefford on 10/20/15.
 */

@RestController
@RequestMapping("/api")
public class PublicSurveyService {

    private final Logger log = LoggerFactory.getLogger(PublicSurveyService.class);

    private final Base64 base64 = new Base64(true);

    @Inject
    private Environment env;

    @Inject
    private SurveyAccessRepository surveyAccessRepository;

    @Inject
    private SurveyContentRepository surveyContentRepository;

    @Inject
    private SurveyFirstRepository surveyFirstRepository;

    @Inject
    private ReportingService reportingService;

    @Inject
    private SequenceService sequenceService;
    @Inject
    private MailService mailService;

    /**
     * GET
     *
     * @param hashId - the hashid is used to obfuscate the surveycontent id
     * @return {@link SurveyContent} the survey content is used to construct a questionaire
     */
    @RequestMapping(value = "/public/questions/{hashId}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SurveyContent> questionaire(@PathVariable("hashId") String hashId) {
        Query query1 = new Query();
        query1.setKey(ComplexKey.of(hashId));
        query1.setInclusiveEnd(true);
        SurveyAccess access = surveyAccessRepository.findOneByAccessKey(query1).stream().findFirst().get();


        return ResponseEntity.ok().body(surveyContentRepository.findOne(access.getSurveyName()));
    }

    /**
     * POST  /SurveyFirsts -> Create a new surveyFirst.
     */
    @RequestMapping(value = "/public/save",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SurveyFirst> createUpdate(@Valid @RequestBody SurveyFirst surveyFirst) throws URISyntaxException {
        log.debug("REST request to save surveyFirst : {}", surveyFirst);
        if (surveyFirst.getId() != null) {
            if (surveyFirst.getDemographicInfo().getEmail() == null) {
                return ResponseEntity.badRequest().header("Failure", "You must provide and email to receive your report").body(null);
            }
            surveyFirst.setUpdated(new Date());
            mailService.sendReportMail(surveyFirst.getDemographicInfo().getEmail(), createReportUrl(surveyFirst.getReportId()));
        } else {
            surveyFirst.setId(sequenceService.nextId(SurveyFirst.SEQ));

            surveyFirst.setReportId(encodeId(surveyFirst.getId()));

            surveyFirst.setUpdated(new Date());
        }

        SurveyFirst result = surveyFirstRepository.save(surveyFirst);
        return ResponseEntity.ok(result);
    }


    private String createReportUrl(String reportId) {
        String reportUrl = env.getProperty("report.url");
        return reportUrl + reportId;
    }

    /**
     * @param hashId - this hashid is used to obfuscate the surveyfirst id from which we can load the survey content,
     *               user responses and report totals
     * @return Data to construct the report of user input and survey totals
     */
    @RequestMapping(value = "/public/report/{hashId}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity report(@PathVariable("hashId") String hashId) {
        String id = decodeId(hashId);
        Optional<SurveyFirst> surveyFirst = Optional.of(surveyFirstRepository.findOne(id));
        ResponseEntity responseEntity = null;
        if (surveyFirst.isPresent()) {
            Query query2 = new Query();
            query2.setKey(ComplexKey.of(surveyFirst.get().getName()));
            query2.setInclusiveEnd(true);


            List<SurveyFirst> surveyFirstList = new ArrayList<>();
            surveyFirstRepository.findAll().forEach(x -> {
                if (x != null && x.getName() != null
                    && surveyFirst.isPresent()
                    && x.getName().equals(surveyFirst.get().getName())) {
                    surveyFirstList.add(x);
                }
            });
            SurveyResults results = reportingService.buildReport(surveyFirstList);
            results.getQuestions().sort(Comparator.comparing(QuestionResults::getKey));
            responseEntity = ResponseEntity.ok(results);
        } else {
            responseEntity = ResponseEntity.noContent().build();
        }

        if (responseEntity == null) {
            responseEntity = ResponseEntity.notFound().build();
        }

        return responseEntity;
    }

    private String decodeId(String hashId) {

        return new String(base64.decode(hashId.getBytes()));
    }

    private String encodeId(String id) {
        String result = new String(base64.encode(id.getBytes()));
        result = result.replaceAll("\r", "").replaceAll("\n", "");
        return result;
    }

    /**
     * @param hashId - this hashid is used to obfuscate the surveyfirst id from which we can load the survey content,
     *               user responses and report totals
     * @return Data to construct the report of user input and survey totals
     */
    @RequestMapping(value = "/public/load/{hashId}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity load(@PathVariable("hashId") String hashId) {
        String id = decodeId(hashId);
        Optional<SurveyFirst> surveyFirst = Optional.of(surveyFirstRepository.findOne(id));
        ResponseEntity responseEntity = null;
        if (surveyFirst.isPresent()) {
            responseEntity = ResponseEntity.ok(surveyFirst.get());
        } else {
            try {
                Thread.sleep(1000l);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            surveyFirst = Optional.of(surveyFirstRepository.findOne(id));
            responseEntity = null;
            if (surveyFirst.isPresent()) {
                responseEntity = ResponseEntity.ok(surveyFirst.get());
            } else {
                responseEntity = ResponseEntity.notFound().build();
            }
        }

        return responseEntity;
    }
}
