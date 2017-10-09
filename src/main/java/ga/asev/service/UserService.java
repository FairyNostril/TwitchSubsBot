package ga.asev.service;

import ga.asev.client.TwitchApiClient;
import ga.asev.client.model.Token;
import ga.asev.client.model.TokenException;
import ga.asev.storage.UserStorage;
import ga.asev.storage.model.UserData;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class UserService {

    private final TwitchApiClient twitchClient;
    private final UserStorage userStorage;

    public UserService(TwitchApiClient twitchClient, UserStorage userStorage) {
        this.twitchClient = twitchClient;
        this.userStorage = userStorage;
    }

    public String createToken(String code) {
        Token token = twitchClient.createToken(code);
        UserData userData = new UserData(token.getAccessToken(), token.getRefreshToken());
        userStorage.saveUserData(token.getAccessToken(), userData);
        return token.getAccessToken();
    }

    public boolean subscribedToChannel(String userId, String token, String channel) {
        if (token != null) {
            updateUserDataId(token, userId);
        }
        UserData userData = userStorage.getUserData(userId);

        return handleTokenExp(userId, userData, accessToken ->
                twitchClient.subscribedToChannel(accessToken, channel)
        );
    }

    private void updateUserDataId(String oldId, String newId) {
        UserData userData = userStorage.removeUserData(oldId);
        userStorage.saveUserData(newId, userData);
    }

    private <T> T handleTokenExp(String userId, UserData userData, Function<String, T> call) {
        try {
            return call.apply(userData.getAccessToken());
        } catch (TokenException e) {
            Token refreshedToken = twitchClient.refreshToken(userData.getRefreshToken());
            userStorage.saveUserData(userId, new UserData(refreshedToken.getAccessToken(), refreshedToken.getRefreshToken()));
            return call.apply(refreshedToken.getAccessToken());
        }
    }

}
