package institute.patientfocus.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.google.common.collect.Lists;
import institute.patientfocus.domain.SurveyAccess;
import institute.patientfocus.repository.SurveyAccessRepository;
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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing SurveyAccess.
 */
@RestController
@RequestMapping("/api")
public class SurveyAccessResource {

    private final Logger log = LoggerFactory.getLogger(SurveyAccessResource.class);

    @Inject
    private SurveyAccessRepository surveyAccessRepository;

    @Inject
    private SequenceService sequenceService;

    /**
     * POST  /surveyAccess -> Create a new surveyAccess.
     */
    @RequestMapping(value = "/surveyAccess",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SurveyAccess> create(@Valid @RequestBody SurveyAccess access) throws URISyntaxException {
        log.debug("REST request to save SurveyAccess : {}", access);

        access.setId(sequenceService.nextId(SurveyAccess.SEQ));
        access.setAccessKey(RandomUtil.generateActivationKey());
        SurveyAccess result = surveyAccessRepository.save(access);
        return ResponseEntity.created(new URI("/api/surveyAccess/" + access.getId())).body(result);
    }

    /**
     * PUT  /surveyAccess -> Updates an existing surveyAccess.
     */
    @RequestMapping(value = "/surveyAccess",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SurveyAccess> update(@Valid @RequestBody SurveyAccess surveyAccess) throws URISyntaxException {
        log.debug("REST request to update SurveyAccess : {}", surveyAccess);
        if (surveyAccess.getId() == null) {
            return create(surveyAccess);
        }
        SurveyAccess result = surveyAccessRepository.save(surveyAccess);
        return ResponseEntity.ok().body(result);
    }

    /**
     * GET  /surveyAccess -> get all the surveyAccess.
     */
    @RequestMapping(value = "/surveyAccess",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<SurveyAccess>> getAll(@RequestParam(value = "page", required = false) Integer offset,
                                                     @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        List<SurveyAccess> page = Lists.newArrayList(surveyAccessRepository.findAll())
            .stream().filter(x -> x != null).collect(Collectors.toList());

        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    /**
     * GET  /surveyAccess/:id -> get the "id" surveyAccess.
     */
    @RequestMapping(value = "/surveyAccess/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SurveyAccess> get(@PathVariable String id) {
        log.debug("REST request to get SurveyAccess : {}", id);
        return Optional.ofNullable(surveyAccessRepository.findOne(id))
            .map(surveyAccess -> new ResponseEntity<>(
                surveyAccess,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /surveyAccess/:id -> delete the "id" surveyAccess.
     */
    @RequestMapping(value = "/surveyAccess/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable String id) {
        log.debug("REST request to delete SurveyAccess : {}", id);
        surveyAccessRepository.delete(id);
    }
}
