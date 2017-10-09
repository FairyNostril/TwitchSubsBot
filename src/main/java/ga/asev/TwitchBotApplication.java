package ga.asev;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.telegram.telegrambots.ApiContextInitializer;

@SpringBootApplication
@EnableCaching
public class TwitchBotApplication {

	public static void main(String[] args) {
        ApiContextInitializer.init();
		SpringApplication.run(TwitchBotApplication.class, args);
	}
}
