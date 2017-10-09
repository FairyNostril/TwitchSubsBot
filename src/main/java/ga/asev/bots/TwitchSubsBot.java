package ga.asev.bots;

import ga.asev.client.model.TokenException;
import ga.asev.message.Messages;
import ga.asev.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import static ga.asev.util.MessageUtil.*;
import static java.util.Collections.singletonList;

@Component
public class TwitchSubsBot extends TelegramLongPollingBot {
    private static final Logger LOG = LoggerFactory.getLogger(TwitchSubsBot.class);

    @Value("${twitchSubs.twitch.authUri}")
    private String authUri;
    @Value("${twitchSubs.telegram.bot.username}")
    private String botUsername;
    @Value("${twitchSubs.telegram.bot.token}")
    private String botToken;

    private final UserService userService;
    private final Messages messages;

    public TwitchSubsBot(UserService userService, Messages messages) {
        this.userService = userService;
        this.messages = messages;
    }

    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            if (isStartMessage(update)) {
                checkSub(update);
            } else {
                authorize(update);
            }
        }
    }

    private void checkSub(Update update) {
        try {
            String userId = update.getMessage().getFrom().getId().toString();
            String token = getTokenFromPayload(update);
            boolean subscribed = userService.subscribedToChannel(userId, token, "olyashaa");
            if (subscribed) {
                sendText(update, message("message.subscribed", update));
            } else {
                sendText(update, message("message.notsubscribed", update));
            }
        } catch (TokenException e) {
            authorize(update);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            sendText(update, message("message.error", update));
        }
    }

    private String getTokenFromPayload(Update update) {
        return getStartPayload(update.getMessage().getText());
    }

    private void authorize(Update update) {
        SendMessage message = new SendMessage()
                .setChatId(update.getMessage().getChatId())
                .setReplyMarkup(new InlineKeyboardMarkup().setKeyboard(singletonList(singletonList(
                        new InlineKeyboardButton(message("message.authorize.button", update))
                                .setUrl(authUri)
                ))))
                .setText(message("message.authorize.hi", update));
        send(message);
    }

    private void sendText(Update update, String text) {
        SendMessage message = new SendMessage()
                .setChatId(update.getMessage().getChatId())
                .setText(text);
        send(message);
    }

    private void send(SendMessage message) {
        try {
            sendApiMethod(message);
        } catch (TelegramApiException e) {
            LOG.error(e.getMessage(), e);
        }
    }

    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    private String message(String code, Update update) {
        return messages.getMessage(code, languageCode(update));
    }

}