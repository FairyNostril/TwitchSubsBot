package ga.asev.rest;

import ga.asev.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/api/auth")
public class AuthApiController {
    private static final Logger LOG = LoggerFactory.getLogger(AuthApiController.class);

    private final UserService userService;
    private final String redirectUri;

    public AuthApiController(UserService userService, @Value("${twitchSubs.telegram.bot.uri}") String botUri) {
        this.userService = userService;
        this.redirectUri = "redirect:" + botUri + "?start=";
    }

    @GetMapping("/twitch")
    public String authTwitch(@RequestParam("code") String code) {
        try {
            return redirectUri + userService.createToken(code);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return redirectUri + "error";
        }
    }
}
