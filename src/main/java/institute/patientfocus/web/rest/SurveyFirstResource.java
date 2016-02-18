package institute.patientfocus.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.couchbase.client.protocol.views.ComplexKey;
import com.couchbase.client.protocol.views.Query;
import com.google.common.collect.Lists;
import institute.patientfocus.domain.SurveyFirst;
import institute.patientfocus.domain.SurveyResults;
import institute.patientfocus.repository.SurveyFirstRepository;
import institute.patientfocus.repository.SurveyResultsRepository;
import institute.patientfocus.service.ReportingService;
import institute.patientfocus.service.SequenceService;
import institute.patientfocus.service.util.RandomUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing surveyFirst.
 */
@RestController
@RequestMapping("/api")
public class SurveyFirstResource {

    private final Logger log = LoggerFactory.getLogger(SurveyFirstResource.class);

    @Inject
    private SurveyFirstRepository surveyFirstRepository;

    @Inject
    private SurveyResultsRepository resultsRepository;

    @Inject
    private ReportingService reportingService;

    @Inject
    private SequenceService sequenceService;

    /**
     * POST  /SurveyFirsts -> Create a new surveyFirst.
     */
    @RequestMapping(value = "/surveyFirst",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SurveyFirst> create(@Valid @RequestBody SurveyFirst surveyFirst) throws URISyntaxException {
        log.debug("REST request to save surveyFirst : {}", surveyFirst);
        if (surveyFirst.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new surveyFirst cannot already have an ID").body(null);
        }
        surveyFirst.setId(sequenceService.nextId(SurveyFirst.SEQ));
        surveyFirst.setReportId(RandomUtil.generateActivationKey());
        surveyFirst.setUpdated(new Date());
        SurveyFirst result = surveyFirstRepository.save(surveyFirst);
        return ResponseEntity.created(new URI("/api/surveyFirsts/" + surveyFirst.getId())).body(result);
    }

    /**
     * PUT  /SurveyFirsts -> Updates an existing surveyFirst.
     */
    @RequestMapping(value = "/surveyFirst",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SurveyFirst> update(@Valid @RequestBody SurveyFirst surveyFirst) throws URISyntaxException {
        log.debug("REST request to update surveyFirst : {}", surveyFirst);
        if (surveyFirst.getId() == null) {
            return create(surveyFirst);
        }
        SurveyFirst result = surveyFirstRepository.save(surveyFirst);
        return ResponseEntity.ok().body(result);
    }

    /**
     * GET  /SurveyFirsts -> get all the SurveyFirsts.
     */
    @RequestMapping(value = "/surveyFirst",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<SurveyFirst>> getAll(@RequestParam(value = "page", required = false) Integer offset,
                                                    @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        List<SurveyFirst> page = Lists.newArrayList(surveyFirstRepository.findAll())
            .stream().filter(surveyFirst -> surveyFirst != null).collect(Collectors.toList());

        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    /**
     * GET  /SurveyFirsts/:id -> get the "id" surveyFirst.
     */
    @RequestMapping(value = "/surveyFirst/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SurveyFirst> get(@PathVariable String id) {
        log.debug("REST request to get surveyFirst : {}", id);
        return Optional.ofNullable(surveyFirstRepository.findOne(id))
            .map(surveyFirst -> new ResponseEntity<>(
                surveyFirst,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /SurveyFirsts/:id -> delete the "id" surveyFirst.
     */
    @RequestMapping(value = "/surveyFirst/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable String id) {
        log.debug("REST request to delete surveyFirst : {}", id);
        surveyFirstRepository.delete(id);
    }

    /**
     * DELETE  /SurveyFirsts/:id -> delete the "id" surveyFirst.
     */
    @RequestMapping(value = "/surveyFirst/clear/{surveyName}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void deletebyName(@PathVariable String surveyName, @RequestParam("type") String type) {
        log.debug("REST request to delete surveyFirst : {}", surveyName);
        Query query = new Query();
        query.setKey(ComplexKey.of(surveyName));
        query.setInclusiveEnd(true);
        List<SurveyFirst> surveyFirstList = surveyFirstRepository.findByName(query).stream().filter(x -> x != null).collect(Collectors.toList());
        if ("survey".equals(type)) {
            surveyFirstList.stream().forEach(x -> x.setName("x-" + x.getName()));
            surveyFirstRepository.save(surveyFirstList);
        } else if ("hard".equals(type)) {
            surveyFirstRepository.delete(surveyFirstList);
        } else if ("ALL".equals(type)) {
            surveyFirstRepository.deleteAll();
        }
    }


    /**
     * Create Report based on survey taken
     */
    @RequestMapping(value = "/surveyFirst/report/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SurveyResults> loadRealTimeReport(@PathVariable String id) {

        Query query = new Query();
        query.setKey(ComplexKey.of(id, 0));
        query.setInclusiveEnd(true);

        SurveyResults results = reportingService.buildReport(surveyFirstRepository.findByNameAndBatch(query));
        return new ResponseEntity<SurveyResults>(results, HttpStatus.OK);

    }
}
