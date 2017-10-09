package ga.asev.util;

import org.telegram.telegrambots.api.objects.Update;

public final class MessageUtil {
    private MessageUtil() {
    }

    public static boolean isStartMessage(Update update) {
        return update.getMessage().getText().equals("/start") || update.getMessage().getText().startsWith("/start ");
    }

    public static String getStartPayload(String message) {
        String[] args = message.split(" ");
        if (args.length > 1) {
            return args[1];
        }
        return null;
    }

    public static String languageCode(Update update) {
        if (update.getMessage().getFrom() == null) {
            return null;
        }
        return update.getMessage().getFrom().getLanguageCode();
    }
}
