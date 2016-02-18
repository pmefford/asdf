package institute.patientfocus.domain;

import java.util.Locale;

/**
 * Created by pmefford on 10/3/15.
 */
public class LocalizedText {
    private Locale locale;
    private String fullText;

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public String getFullText() {
        return fullText;
    }

    public void setFullText(String fullText) {
        this.fullText = fullText;
    }
}
