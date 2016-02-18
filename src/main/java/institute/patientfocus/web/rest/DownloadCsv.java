package institute.patientfocus.web.rest;

import com.couchbase.client.protocol.views.ComplexKey;
import com.couchbase.client.protocol.views.Query;
import institute.patientfocus.domain.LocalizedText;
import institute.patientfocus.domain.QuestionResponse;
import institute.patientfocus.domain.SurveyContent;
import institute.patientfocus.domain.SurveyFirst;
import institute.patientfocus.repository.SurveyContentRepository;
import institute.patientfocus.repository.SurveyFirstRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by pmefford on 11/13/15.
 */
@Controller
public class DownloadCsv {

    @Autowired
    private SurveyFirstRepository surveyFirstRepository;

    @Autowired
    private SurveyContentRepository surveyContentRepository;

    @RequestMapping(value = "/download/{surveyName}", produces = "text/csv")
    public void downloadCSV(@PathVariable("surveyName") String surveyName, HttpServletResponse response) throws IOException {

        response.setContentType("text/csv");
        String reportName = "CSV_" + surveyName.toUpperCase() + ".csv";
        response.setHeader("Content-disposition", "attachment;filename=" + reportName);

        ArrayList<String> rows = new ArrayList<String>();
        Query query = new Query();
        query.setInclusiveEnd(true);
        query.setKey(ComplexKey.of(surveyName));
        List<SurveyFirst> surveyFirstList = surveyFirstRepository.findByName(query);
        SurveyContent surveyContent = surveyContentRepository.findOne(surveyName);

            Collections.sort(surveyContent.getQuestions(),
                (a1, a2) -> {
                    Integer first = Integer.parseInt(a1.getKey().substring(1, a1.getKey().length()));
                    Integer second = Integer.parseInt(a2.getKey().substring(1, a2.getKey().length()));
                    return first.compareTo(second);
                }
            );
            String row1 = "";
            String row2 = "";
            for (int i = 0; i < surveyContent.getQuestions().size(); i++) {
                row1 += surveyContent.getQuestions().get(i).getKey() +
                    ",";

                //get the question text
                List<LocalizedText> lt = surveyContent.getQuestions().get(i).getText();
                String text = (lt != null && lt.size() > 0) ? lt.get(0).getFullText().replaceAll(",", ";") : "";
                row2 += text + ",";
            }
        row1 += "d1,d2,d3,d4,d5,d6,accessKey,";
        row2 += "country,email,industry,function,organization,orgSize,accessKey,";
            response.getOutputStream().print(row1.substring(0, row1.length() - 1) + "\n");

            response.getOutputStream().print(row2.substring(0, row2.length() - 1) + "\n");

        if (!surveyFirstList.isEmpty()) {
            for (SurveyFirst surveyFirst : surveyFirstList) {
                Collections.sort(surveyFirst.getQuestionList(),
                    (a1, a2) -> {
                        Integer first = Integer.parseInt(a1.getQuestionKey().substring(1, a1.getQuestionKey().length()));
                        Integer second = Integer.parseInt(a2.getQuestionKey().substring(1, a2.getQuestionKey().length()));
                        return first.compareTo(second);
                    });
                if (!surveyFirst.getQuestionList().isEmpty()) {
                    String otherRows = "";
                    for (QuestionResponse questionResponse : surveyFirst.getQuestionList()) {
                        otherRows += questionResponse.getAnswers() + ",";
                    }
                    if (surveyFirst.getDemographicInfo() != null) {
                        otherRows += removeComma(surveyFirst.getDemographicInfo().getCountry()) + ",";
                        otherRows += removeComma(surveyFirst.getDemographicInfo().getEmail()) + ",";
                        otherRows += removeComma(surveyFirst.getDemographicInfo().getIndustry()) + ",";
                        otherRows += removeComma(surveyFirst.getDemographicInfo().getJobFunction()) + ",";
                        otherRows += removeComma(surveyFirst.getDemographicInfo().getOrganization()) + ",";
                        otherRows += removeComma(surveyFirst.getDemographicInfo().getOrgSize()) + ",";
                    } else {
                        otherRows += ",,,,,,";
                    }
                    otherRows += surveyFirst.getAccessKey() + ",";
                    response.getOutputStream().print(otherRows.substring(0, otherRows.length() - 1) + "\n");
                }
            }
        } else {
            response.getOutputStream().print("0 results were found" + "\n");
        }

        response.getOutputStream().flush();

    }

    private String removeComma(String input) {
        String result = "";
        if (input != null && input.contains(",")) {
            result = input.replaceAll(",", " ");
        } else {
            result = input;
        }

        return result;
    }
}
