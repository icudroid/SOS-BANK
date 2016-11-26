package net.dkahn.starter.services.security.impl;

import net.dkahn.starter.domains.security.Pinpad;
import net.dkahn.starter.services.security.IPinpadService;
import net.dkahn.starter.services.security.PinpadConfigProperties;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@EnableJpaRepositories
@EnableTransactionManagement
@EntityScan(basePackages = {"net.dkahn.starter.domains"})
@ComponentScan
public class PinpadServiceTest {

    @Autowired
    private IPinpadService pinpadService;
    @Autowired
    private PinpadConfigProperties pinpadConfigProperties;

    @org.junit.Before
    public void setUp() throws Exception {

    }

    @org.junit.Test
    public void generate() throws Exception {
        pinpadService.generate();

    }

    @org.junit.Test
    public void decodePassword() throws Exception {
        Pinpad generate = pinpadService.generate();

        List<Integer> correspondance = generate.getCorrespondance();
        String pwd = correspondance.get(0).toString()+correspondance.get(0).toString()+correspondance.get(0).toString()+correspondance.get(0).toString()+correspondance.get(0).toString()+correspondance.get(1).toString();

        String pwdDecoded = pinpadService.decodePassword(generate.getId(), "000001");

        assertThat(pwdDecoded).isEqualTo(pwd);
    }

    @org.junit.Test
    public void findPathImage() throws Exception {
        Pinpad pinpad = pinpadService.generate();
        List<Integer> correspondance = pinpad.getCorrespondance();


        for (int i = 0; i < correspondance.size(); i++) {
            String imgPath = pinpadConfigProperties.getBase() + correspondance.get(i).toString() +
                    pinpadConfigProperties.getImageExtention();

            String pathImage = pinpadService.findPathImage(pinpad.getId(), i);

            assertThat(pathImage).isEqualTo(imgPath);
        }

    }

    @org.junit.Test
    public void generateImage() throws Exception {
        Pinpad pinpad = pinpadService.generate();
        pinpadService.generateImage(pinpad.getId());
    }

}