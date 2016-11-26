package net.dkahn.starter.services.security;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuratin du pinpad
 */
@Data
@ConfigurationProperties("pinpad")
public class PinpadConfigProperties {
    private final int length = 10;
    private final int imageWidth = 46;
    private final int imageHeight = 46;
    private final String imageExtention = ".png";
    private final int imageTotalHeight = imageWidth * imageHeight;

    private final String base;
    private final Integer duration=120;
}
