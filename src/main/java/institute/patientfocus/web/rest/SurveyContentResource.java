package institute.patientfocus.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.google.common.collect.Lists;
import institute.patientfocus.domain.SurveyContent;
import institute.patientfocus.repository.SurveyContentRepository;
import institute.patientfocus.service.SequenceService;
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
 * REST controller for managing surveyContent.
 */
@RestController
@RequestMapping("/api")
public class SurveyContentResource {

    private final Logger log = LoggerFactory.getLogger(SurveyContentResource.class);

    @Inject
    private SurveyContentRepository surveyContentRepository;

    @Inject
    private SequenceService sequenceService;

    /**
     * POST  /SurveyContents -> Create a new surveyContent.
     */
    @RequestMapping(value = "/surveyContent",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SurveyContent> create(@Valid @RequestBody SurveyContent surveyContent) throws URISyntaxException {
        log.debug("REST request to save surveyContent : {}", surveyContent);
        if (surveyContent.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new surveyContent must have an ID").body(null);
        }
        surveyContent.setId(sequenceService.nextId(SurveyContent.SEQ));
        SurveyContent result = surveyContentRepository.save(surveyContent);
        return ResponseEntity.created(new URI("/api/surveyContents/" + surveyContent.getName())).body(result);
    }

    /**
     * PUT  /SurveyContents -> Updates an existing surveyContent.
     */
    @RequestMapping(value = "/surveyContent",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SurveyContent> update(@Valid @RequestBody SurveyContent surveyContent) throws URISyntaxException {
        log.debug("REST request to update surveyContent : {}", surveyContent);
        if (surveyContent.getId() == null) {
            return create(surveyContent);
        }
        SurveyContent result = surveyContentRepository.save(surveyContent);
        return ResponseEntity.ok().body(result);
    }

    /**
     * GET  /SurveyContents -> get all the SurveyContents.
     */
    @RequestMapping(value = "/surveyContent",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<SurveyContent>> getAll(@RequestParam(value = "page", required = false) Integer offset,
                                                      @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        List<SurveyContent> page = Lists.newArrayList(surveyContentRepository.findAll())
            .stream().filter(x -> x != null).collect(Collectors.toList());

        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    /**
     * GET  /SurveyContents/:id -> get the "id" surveyContent.
     */
    @RequestMapping(value = "/surveyContent/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SurveyContent> get(@PathVariable String id) {
        log.debug("REST request to get surveyContent : {}", id);
        return Optional.ofNullable(surveyContentRepository.findOne(id))
            .map(surveyContent -> new ResponseEntity<>(
                surveyContent,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /SurveyContents/:id -> delete the "id" surveyContent.
     */
    @RequestMapping(value = "/surveyContent/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable String id) {
        log.debug("REST request to delete surveyContent : {}", id);
        surveyContentRepository.delete(id);
    }


}
