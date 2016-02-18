package institute.patientfocus.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.google.common.collect.Lists;
import institute.patientfocus.domain.SurveyResults;
import institute.patientfocus.repository.SurveyResultsRepository;
import institute.patientfocus.service.SequenceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing surveyFirst.
 */
@RestController
@RequestMapping("/api")
public class SurveyResultsResource {

    private final Logger log = LoggerFactory.getLogger(SurveyResultsResource.class);

    @Inject
    private SurveyResultsRepository surveyResultsRepository;

    @Inject
    private SequenceService sequenceService;


    /**
     * GET  /SurveyFirsts -> get all the SurveyFirsts.
     */
    @RequestMapping(value = "/surveyResult",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<SurveyResults>> getAll(@RequestParam(value = "page", required = false) Integer offset,
                                                      @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        List<SurveyResults> page = Lists.newArrayList(surveyResultsRepository.findAll())
            .stream().filter(x -> x != null).collect(Collectors.toList());

        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    /**
     * GET  /SurveyFirsts/:id -> get the "id" surveyFirst.
     */
    @RequestMapping(value = "/surveyResult/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SurveyResults> get(@PathVariable String id) {
        log.debug("REST request to get surveyFirst : {}", id);
        return Optional.ofNullable(surveyResultsRepository.findOne(id))
            .map(surveyFirst -> new ResponseEntity<>(
                surveyFirst,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

}
