package institute.patientfocus.web.rest;

import com.google.common.collect.Lists;
import institute.patientfocus.Application;
import institute.patientfocus.domain.Tester;
import institute.patientfocus.repository.TesterRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the TesterResource REST controller.
 *
 * @see TesterResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class TesterResourceTest {


    private static final Integer DEFAULT_FIELD1 = 0;
    private static final Integer UPDATED_FIELD1 = 1;
    private static final String DEFAULT_FIELD2 = "SAMPLE_TEXT";
    private static final String UPDATED_FIELD2 = "UPDATED_TEXT";

    @Inject
    private TesterRepository testerRepository;

    private MockMvc restTesterMockMvc;

    private Tester tester;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TesterResource testerResource = new TesterResource();
        ReflectionTestUtils.setField(testerResource, "testerRepository", testerRepository);
        this.restTesterMockMvc = MockMvcBuilders.standaloneSetup(testerResource).build();
    }

    @Before
    public void initTest() {
        testerRepository.deleteAll();
        tester = new Tester();
        tester.setField1(DEFAULT_FIELD1);
        tester.setField2(DEFAULT_FIELD2);
    }

    @Test
    public void createTester() throws Exception {
        int databaseSizeBeforeCreate = Lists.newArrayList(testerRepository.findAll())
            .stream().filter(x -> x != null).collect(Collectors.toList()).size();

        // Create the Tester
        restTesterMockMvc.perform(post("/api/testers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tester)))
            .andExpect(status().isCreated());

        // Validate the Tester in the database
        List<Tester> testers = Lists.newArrayList(testerRepository.findAll())
            .stream().filter(x -> x != null).collect(Collectors.toList());
        assertThat(testers).hasSize(databaseSizeBeforeCreate + 1);
        Tester testTester = testers.get(testers.size() - 1);
        assertThat(testTester.getField1()).isEqualTo(DEFAULT_FIELD1);
        assertThat(testTester.getField2()).isEqualTo(DEFAULT_FIELD2);
    }

    @Test
    public void getAllTesters() throws Exception {
        // Initialize the database
        testerRepository.save(tester);

        // Get all the testers
        restTesterMockMvc.perform(get("/api/testers"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tester.getId())))
            .andExpect(jsonPath("$.[*].field1").value(hasItem(DEFAULT_FIELD1)))
            .andExpect(jsonPath("$.[*].field2").value(hasItem(DEFAULT_FIELD2.toString())));
    }

    @Test
    public void getTester() throws Exception {
        // Initialize the database
        testerRepository.save(tester);

        // Get the tester
        restTesterMockMvc.perform(get("/api/testers/{id}", tester.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(tester.getId()))
            .andExpect(jsonPath("$.field1").value(DEFAULT_FIELD1))
            .andExpect(jsonPath("$.field2").value(DEFAULT_FIELD2.toString()));
    }

    @Test
    public void getNonExistingTester() throws Exception {
        // Get the tester
        restTesterMockMvc.perform(get("/api/testers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateTester() throws Exception {
        // Initialize the database
        testerRepository.save(tester);

        int databaseSizeBeforeUpdate = Lists.newArrayList(testerRepository.findAll()).size();

        // Update the tester
        tester.setField1(UPDATED_FIELD1);
        tester.setField2(UPDATED_FIELD2);
        restTesterMockMvc.perform(put("/api/testers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tester)))
            .andExpect(status().isOk());

        // Validate the Tester in the database
        List<Tester> testers = Lists.newArrayList(testerRepository.findAll())
            .stream().filter(x -> x != null).collect(Collectors.toList());
        assertThat(testers).hasSize(databaseSizeBeforeUpdate);
        Tester testTester = testers.get(testers.size() - 1);
        assertThat(testTester.getField1()).isEqualTo(UPDATED_FIELD1);
        assertThat(testTester.getField2()).isEqualTo(UPDATED_FIELD2);
    }

    @Test
    public void deleteTester() throws Exception {
        // Initialize the database
        testerRepository.save(tester);

        int databaseSizeBeforeDelete = Lists.newArrayList(testerRepository.findAll())
            .stream().filter(x -> x != null).collect(Collectors.toList()).size();

        // Get the tester
        restTesterMockMvc.perform(delete("/api/testers/{id}", tester.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Tester> testers = Lists.newArrayList(testerRepository.findAll())
            .stream().filter(x -> x != null).collect(Collectors.toList());
        assertThat(testers).hasSize(databaseSizeBeforeDelete - 1);
    }
}
