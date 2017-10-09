package ga.asev.client.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class Subscription implements Serializable {
    @JsonProperty("channel")
    private Channel channel = new Channel();

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public static class Channel {
        @JsonProperty("name")
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
