import play.Application;
import play.GlobalSettings;
import play.data.format.Formatters;
import utils.AnnotationDateFormatter;

import java.lang.annotation.Annotation;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Global extends GlobalSettings {

    public void onStart(Application app) {

        Formatters.register(Date.class, new Formatters.SimpleFormatter<Date>() {

            private  static final String PATTERN = "dd-mm-yyyy";

            @Override
            public Date parse(String text, Locale locale) throws ParseException {

                if (text == null || text.trim().isEmpty())
                {
                    return null;
                }
                SimpleDateFormat sdf = new SimpleDateFormat(PATTERN, locale);
                sdf.setLenient(false);
                return sdf.parse(text);
            }

            @Override
            public String print(Date value, Locale locale) {
                if (value == null) {
                    return "";
                }
                return new SimpleDateFormat(PATTERN, locale).format(value);
            }
        });

        Formatters.register(Date.class, new AnnotationDateFormatter());
    }

}
