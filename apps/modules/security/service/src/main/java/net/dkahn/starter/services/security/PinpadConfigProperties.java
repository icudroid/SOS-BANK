package net.dkahn.starter.services.security;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Configuratin du pinpad
 */
@Configuration
@Data
@ConfigurationProperties(prefix = "pinpad" , ignoreUnknownFields = true)
public class PinpadConfigProperties {
    private int length = 10;
    private int imageWidth = 46;
    private int imageHeight = 46;
    private String imageExtention = ".png";
    private String base;
    private Integer duration=120;
}
