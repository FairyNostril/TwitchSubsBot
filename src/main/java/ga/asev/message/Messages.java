package ga.asev.message;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class Messages {
    private final MessageSource messageSource;

    public Messages(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public String getMessage(String code, String locale) {
        return messageSource.getMessage(code, null, Locale.forLanguageTag(locale));
    }
}
