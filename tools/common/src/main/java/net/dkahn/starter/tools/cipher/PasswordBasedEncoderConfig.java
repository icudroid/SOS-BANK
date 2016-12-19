package net.dkahn.starter.tools.cipher;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "pbe")
public class PasswordBasedEncoderConfig {

    private String salt     = "DEFAULT";
    private int iterations  = 2000;
    private int keyLength   = 256;
    private String crypt    = "AES/CTR/NOPADDING";
    private String secret   = "PBEWITHSHA256AND256BITAES-CBC-BC";
}
