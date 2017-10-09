package ga.asev.client;

import ga.asev.client.model.Subscription;
import ga.asev.client.model.Token;
import ga.asev.client.model.RootEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.ws.rs.NotFoundException;

import static org.springframework.util.StringUtils.isEmpty;

@Component
public class TwitchApiClient extends BaseTwitchApiClient {

    private final String tokenUri;
    private final String refreshTokenUri;
    private final String baseUri;

    public TwitchApiClient(@Value("${twitchSubs.twitch.tokenUri}") String tokenUri,
                           @Value("${twitchSubs.twitch.refreshTokenUri}") String refreshTokenUri,
                           @Value("${twitchSubs.twitch.baseUri}") String baseUri) {
        this.tokenUri = tokenUri;
        this.refreshTokenUri = refreshTokenUri;
        this.baseUri = baseUri;
    }


    public Token createToken(String code) {
        return post(tokenUri + "&code=" + code, Token.class);
    }

    public Token refreshToken(String refreshToken) {
        return post(refreshTokenUri + "&refresh_token=" + refreshToken, Token.class);
    }

    public boolean subscribedToChannel(String token, String channelName) {
        if (isEmpty(channelName)) return false;
        try {
            String userName = getUserName(token);
            return subscribedToChannel(userName, channelName, token);
        } catch (NotFoundException e) {
            return false;
        }
    }

    private String getUserName(String token) {
        RootEntity rootEntity = get(baseUri + "/kraken", RootEntity.class, token);
        return rootEntity.getToken().getUserName();
    }

    private boolean subscribedToChannel(String userName, String channelName, String token) {
        String subscriptionUri = String.format(baseUri + "/kraken/users/%s/subscriptions/%s", userName, channelName);
        Subscription subscription = get(subscriptionUri, Subscription.class, token);
        return channelName.toLowerCase().equals(subscription.getChannel().getName().toLowerCase());
    }
}
