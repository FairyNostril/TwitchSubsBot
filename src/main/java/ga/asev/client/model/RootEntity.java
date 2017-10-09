package ga.asev.client.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class RootEntity implements Serializable {

    @JsonProperty("token")
    private TokenInfo token = new TokenInfo();

    public TokenInfo getToken() {
        return token;
    }

    public void setToken(TokenInfo token) {
        this.token = token;
    }

    public static class TokenInfo implements Serializable {

        @JsonProperty("user_name")
        private String userName;

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }
    }
}
