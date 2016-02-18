package institute.patientfocus.web.rest;

import com.google.common.collect.Lists;
import institute.patientfocus.Application;
import institute.patientfocus.config.CouchbaseConfig;
import institute.patientfocus.domain.QuestionResponse;
import institute.patientfocus.domain.SurveyFirst;
import institute.patientfocus.repository.SurveyFirstRepository;
import institute.patientfocus.service.SequenceService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * Test class for the SurveyFirstResource REST controller.
 *
 * @see SurveyFirstResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
@Import(CouchbaseConfig.class)
public class SurveyFirstResourceTest {


    @Inject
    private SurveyFirstRepository surveyFirstRepository;

    @Inject
    private SequenceService sequenceService;

    private MockMvc restSurveyFirstMockMvc;

    private SurveyFirst surveyFirst;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SurveyFirstResource surveyFirstResource = new SurveyFirstResource();
        ReflectionTestUtils.setField(surveyFirstResource, "surveyFirstRepository", surveyFirstRepository);
        ReflectionTestUtils.setField(surveyFirstResource, "sequenceService", sequenceService);
        this.restSurveyFirstMockMvc = MockMvcBuilders.standaloneSetup(surveyFirstResource).build();
    }

    @Before
    public void initTest() {
        surveyFirstRepository.deleteAll();
        surveyFirst = new SurveyFirst();
        List<QuestionResponse> questionResponses = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 11; i++) {
            QuestionResponse questionResponse = new QuestionResponse();
            questionResponse.setQuestionKey("question-" + i);
            questionResponse.setAnswers(random.nextInt(4) + 1 + "");
            questionResponses.add(questionResponse);
        }
        surveyFirst.setQuestionList(questionResponses);
    }

    @Test
    public void createSurveyFirst() throws Exception {
        List surveys = Lists.newArrayList(surveyFirstRepository.findAll()).stream().filter(x -> x != null).collect(Collectors.toList());
        int databaseSizeBeforeCreate = surveys.size();


        // Create the SurveyFirst
        restSurveyFirstMockMvc.perform(post("/api/surveyFirsts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(surveyFirst)))
            .andExpect(status().isCreated());

        // Validate the SurveyFirst in the database

        List<SurveyFirst> surveyFirsts = Lists.newArrayList(surveyFirstRepository.findAll())
            .stream().filter(x -> x != null).collect(Collectors.toList());
        assertThat(surveyFirsts).hasSize(databaseSizeBeforeCreate + 1);
        SurveyFirst testSurveyFirst = surveyFirsts.get(surveyFirsts.size() - 1);
        assertThat(testSurveyFirst != null);
    }

    @Test
    public void getAllSurveyFirsts() throws Exception {
        // Initialize the database
        surveyFirstRepository.save(surveyFirst);

        // Get all the testers
//        restSurveyFirstMockMvc.perform(get("/api/surveyFirsts"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.[*].id").value(hasItem(surveyFirst.getId())))
//                .andExpect(jsonPath("$.[*].field1").value(hasItem(DEFAULT_FIELD1)))
//                .andExpect(jsonPath("$.[*].field2").value(hasItem(DEFAULT_FIELD2.toString())));
    }

    @Test
    public void getSurveyFirst() throws Exception {
        // Initialize the database
        surveyFirstRepository.save(surveyFirst);

        // Get the surveyFirst
//        restSurveyFirstMockMvc.perform(get("/api/surveyFirsts/{id}", surveyFirst.getId()))
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//            .andExpect(jsonPath("$.id").value(surveyFirst.getId()))
//            .andExpect(jsonPath("$.field1").value(DEFAULT_FIELD1))
//            .andExpect(jsonPath("$.field2").value(DEFAULT_FIELD2.toString()));
    }

    @Test
    public void getNonExistingSurveyFirst() throws Exception {
        // Get the surveyFirst
        restSurveyFirstMockMvc.perform(get("/api/surveyFirsts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateSurveyFirst() throws Exception {
        // Initialize the database
        surveyFirstRepository.save(surveyFirst);

        int databaseSizeBeforeUpdate = Lists.newArrayList(surveyFirstRepository.findAll())
            .stream().filter(x -> x != null).collect(Collectors.toList()).size();

        restSurveyFirstMockMvc.perform(put("/api/surveyFirsts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(surveyFirst)))
            .andExpect(status().isOk());

        // Validate the SurveyFirst in the database
        List<SurveyFirst> testers = Lists.newArrayList(surveyFirstRepository.findAll())
            .stream().filter(x -> x != null).collect(Collectors.toList());
        assertThat(testers).hasSize(databaseSizeBeforeUpdate);
        SurveyFirst testSurveyFirst = testers.get(testers.size() - 1);
//        assertThat(testSurveyFirst.getField1()).isEqualTo(UPDATED_FIELD1);
//        assertThat(testSurveyFirst.getField2()).isEqualTo(UPDATED_FIELD2);
    }

    @Test
    public void deleteSurveyFirst() throws Exception {
        // Initialize the database
        surveyFirstRepository.save(surveyFirst);

        int databaseSizeBeforeDelete = Lists.newArrayList(surveyFirstRepository.findAll())
            .stream().filter(x -> x != null).collect(Collectors.toList()).size();

        // Get the surveyFirst
        restSurveyFirstMockMvc.perform(delete("/api/surveyFirsts/{id}", surveyFirst.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<SurveyFirst> testers = Lists.newArrayList(surveyFirstRepository.findAll())
            .stream().filter(x -> x != null).collect(Collectors.toList());
        assertThat(testers).hasSize(databaseSizeBeforeDelete - 1);
    }
}
