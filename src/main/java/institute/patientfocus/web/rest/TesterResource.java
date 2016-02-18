package institute.patientfocus.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.google.common.collect.Lists;
import institute.patientfocus.domain.Tester;
import institute.patientfocus.repository.TesterRepository;
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
 * REST controller for managing Tester.
 */
@RestController
@RequestMapping("/api")
public class TesterResource {

    private final Logger log = LoggerFactory.getLogger(TesterResource.class);

    @Inject
    private TesterRepository testerRepository;

    @Inject
    private SequenceService sequenceService;

    /**
     * POST  /testers -> Create a new tester.
     */
    @RequestMapping(value = "/testers",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Tester> create(@Valid @RequestBody Tester tester) throws URISyntaxException {
        log.debug("REST request to save Tester : {}", tester);

        tester.setId(sequenceService.nextId("tester"));
        Tester result = testerRepository.save(tester);
        return ResponseEntity.created(new URI("/api/testers/" + tester.getId())).body(result);
    }

    /**
     * PUT  /testers -> Updates an existing tester.
     */
    @RequestMapping(value = "/testers",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Tester> update(@Valid @RequestBody Tester tester) throws URISyntaxException {
        log.debug("REST request to update Tester : {}", tester);
        if (tester.getId() == null) {
            return create(tester);
        }
        Tester result = testerRepository.save(tester);
        return ResponseEntity.ok().body(result);
    }

    /**
     * GET  /testers -> get all the testers.
     */
    @RequestMapping(value = "/testers",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Tester>> getAll(@RequestParam(value = "page", required = false) Integer offset,
                                               @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        List<Tester> page = Lists.newArrayList(testerRepository.findAll())
            .stream().filter(x -> x != null).collect(Collectors.toList());

        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    /**
     * GET  /testers/:id -> get the "id" tester.
     */
    @RequestMapping(value = "/testers/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Tester> get(@PathVariable String id) {
        log.debug("REST request to get Tester : {}", id);
        return Optional.ofNullable(testerRepository.findOne(id))
            .map(tester -> new ResponseEntity<>(
                tester,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /testers/:id -> delete the "id" tester.
     */
    @RequestMapping(value = "/testers/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable String id) {
        log.debug("REST request to delete Tester : {}", id);
        testerRepository.delete(id);
    }
}
