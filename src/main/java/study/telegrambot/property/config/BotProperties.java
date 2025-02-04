package study.telegrambot.property.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "bot")
public class BotProperties {
    private String name;
    private String token;
}