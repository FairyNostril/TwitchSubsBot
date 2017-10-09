package ga.asev;

import ga.asev.bots.TwitchSubsBot;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import javax.annotation.PostConstruct;

@Component
public class BotsStarter {

    private TwitchSubsBot twitchSubsBot;

    public BotsStarter(TwitchSubsBot twitchSubsBot) {
        this.twitchSubsBot = twitchSubsBot;
    }

    @PostConstruct
    public void startBots() {

        TelegramBotsApi botsApi = new TelegramBotsApi();

        try {
            botsApi.registerBot(twitchSubsBot);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
