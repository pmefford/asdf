package institute.patientfocus.service;

import institute.patientfocus.domain.*;
import institute.patientfocus.repository.SurveyContentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by pmefford on 10/21/15.
 */
@Service
public class ReportingService {
    private final Logger logger = LoggerFactory.getLogger(ReportingService.class);

    @Autowired
    private SurveyContentRepository surveyContentRepository;

    public SurveyResults buildReport(List<SurveyFirst> responses) {
        SurveyResults results = new SurveyResults();
        if (responses.size() > 0) {
            String surveyName = responses.stream().findFirst().get().getName();
            SurveyContent content = surveyContentRepository.findOne(surveyName);
            Map<String, QuestionContent> questionContentMap = generateContentMap(content);
            Map<String, QuestionResults> questionResultsMap = new HashMap<>();
            for (SurveyFirst s : responses) {
                for (QuestionResponse response : s.getQuestionList()) {
                    if (!"range".equals(questionContentMap.get(response.getQuestionKey()).getType())) {
                        continue;
                    }
                    if (response.getAnswers() == null) {
                        logger.debug("question answer was null: response:{} question:{}", s.getId(), response.getQuestionKey());
                        continue;
                    }
                    if (questionResultsMap.get(response.getQuestionKey()) == null) {
                        questionResultsMap.put(response.getQuestionKey(), newResults(response, questionContentMap));
                    } else {
                        addResponse(questionResultsMap.get(response.getQuestionKey()), response);
                    }
                }
            }

            if (results.getQuestions().isEmpty()) {
                results.setQuestions(new ArrayList<>());
            }
            questionResultsMap.forEach(
                (s, qr) -> {
                    results.getQuestions().add(qr);
                }
            );
            //results.getQuestions().stream().sorted((e1,e2)-> e1.getName().compareTo(e2.getName()));
            results.setSurveyName(surveyName);
            results.setUpdated(new Date());
        }
        return results;
    }

    private Map<String, QuestionContent> generateContentMap(SurveyContent content) {
        Map<String, QuestionContent> contentMap = new HashMap<>();
        for (QuestionContent qc : content.getQuestions()) {
            contentMap.put(qc.getKey(), qc);
        }
        return contentMap;
    }

    private QuestionResults newResults(QuestionResponse response, Map<String, QuestionContent> questionContentMap) {
        QuestionResults qr = null;
        if (response.getAnswers() != null && response.getAnswers().matches("\\d+")) {
            qr = new QuestionResults();
            qr.setKey(response.getQuestionKey());
            AnswerResults ar = new AnswerResults();
            ar.setValue(response.getAnswers());
            ar.setSum(Integer.parseInt(response.getAnswers()));
            ar.setCount(1);
            qr.getAnswers().put(response.getAnswers(), ar);
        }
        return qr;
    }

    public void addResponse(QuestionResults result, QuestionResponse response) {
        AnswerResults ar = result.getAnswers().get(response.getAnswers());
        if (ar == null) {
            ar = new AnswerResults();
            ar.setValue(response.getAnswers());
            ar.setSum(Integer.parseInt(response.getAnswers()));
            ar.setCount(1);
            result.getAnswers().put(response.getAnswers(), ar);
        } else {
            ar.setCount(ar.getCount() + 1);

            if (response.getAnswers() != null && response.getAnswers().matches("\\d+")) {
                ar.setSum(ar.getSum() + Integer.parseInt(response.getAnswers()));
            }
        }

    }
}
