package net.dkahn.starter.tools.cipher;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest
@ComponentScan
public class PasswordBasedEncoderTest {


    @Autowired
    private PasswordBasedEncoder passwordBasedEncoder;

    @Test
    public void encryptAndDecryptAsString() throws Exception {
        String expected = "Hello";
        String passphrase = "pwd";

        byte[] encrypt = passwordBasedEncoder.encrypt(passphrase, expected);
        String decrypt = passwordBasedEncoder.decryptAsString(passphrase, encrypt);

        assertThat(decrypt).isEqualTo(expected);
    }



    @Test
    public void encryptAndDecryptAsBythe() throws Exception {
        String expected = "Hello";
        String passphrase = "pwd";

        byte[] encrypt = passwordBasedEncoder.encrypt(passphrase, expected.getBytes());
        byte[] decrypt = passwordBasedEncoder.decrypt(passphrase, encrypt);

        assertThat(decrypt).isEqualTo(expected.getBytes());
    }
}