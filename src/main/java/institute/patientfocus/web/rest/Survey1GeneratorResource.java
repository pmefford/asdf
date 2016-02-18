package institute.patientfocus.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.google.common.collect.Lists;
import institute.patientfocus.domain.QuestionResponse;
import institute.patientfocus.domain.SurveyFirst;
import institute.patientfocus.repository.SurveyFirstRepository;
import institute.patientfocus.repository.SurveyResultsRepository;
import institute.patientfocus.service.BatchService;
import institute.patientfocus.service.SequenceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Created by pmefford on 9/19/15.
 */

@RestController
@RequestMapping("/api")
public class Survey1GeneratorResource {


    private final Logger log = LoggerFactory.getLogger(Survey1GeneratorResource.class);

    @Inject
    private SurveyFirstRepository surveyFirstRepository;

    @Inject
    private SurveyResultsRepository resultsRepository;

    @Inject
    private SequenceService sequenceService;

    @Inject
    private BatchService batchService;

    /**
     * GET  /SurveyFirsts/:id -> get the "id" surveyFirst.
     */
    @RequestMapping(value = "/survey-1/generate",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void generateResults(@RequestParam("occasion") String occasion, @RequestParam("surveyName") String surveyName) {
        List<String> categories = Arrays.asList("cat1", "cat2", "cat3", "cat4");
        Random random = new Random();
        for (int i = 1; i <= 100; i++) {
            int categoryIndex = 0;
            SurveyFirst surveyFirst = new SurveyFirst();
            List<QuestionResponse> responses = new ArrayList<>();
            surveyFirst.setQuestionList(responses);
            surveyFirst.setOccasion(occasion);
            surveyFirst.setName(surveyName);
            surveyFirst.setBatch(batchService.currentBatch(surveyName));
            surveyFirst.setId(sequenceService.nextId(SurveyFirst.SEQ));
            for (int j = 0; j < 25; j++) {
                QuestionResponse response = new QuestionResponse();
                response.setAnswers(random.nextInt(5) + "");
                response.setQuestionKey("question-" + j);
                responses.add(response);
                if (j > (8 * categoryIndex)) {
                    categoryIndex = (categoryIndex == 4) ? 4 : categoryIndex + 1;
                }
            }
            surveyFirstRepository.save(surveyFirst);
        }
    }

    /**
     * GET  /SurveyFirsts/:id -> get the "id" surveyFirst.
     */
    @RequestMapping(value = "/survey-1/updateAll",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public Integer updateResults(@RequestBody SurveyFirst surveyFirst) {

        List<SurveyFirst> page = Lists.newArrayList(surveyFirstRepository.findAll())
            .stream().filter(x -> x != null).collect(Collectors.toList());
        page.stream().forEach(x -> updateRecord(x, surveyFirst));
        surveyFirstRepository.save(page);
        return page.size();
    }

    private void updateRecord(SurveyFirst x, SurveyFirst surveyFirst) {
        if (surveyFirst.getUpdated() != null) {
            x.setUpdated(surveyFirst.getUpdated());
        }
        if (surveyFirst.getBatch() != null && surveyFirst.getBatch() > 0) {
            x.setBatch(surveyFirst.getBatch());
        }
        if (!StringUtils.isEmpty(surveyFirst.getName())) {
            x.setName(surveyFirst.getName());
        }
        if (!StringUtils.isEmpty(surveyFirst.getOccasion())) {
            x.setOccasion(surveyFirst.getOccasion());
        }

    }




}
